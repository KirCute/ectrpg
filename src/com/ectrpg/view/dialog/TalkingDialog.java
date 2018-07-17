package com.ectrpg.view.dialog;

import com.ectrpg.controller.Keyboard;
import com.ectrpg.db.Localization;
import com.ectrpg.view.GameFrame;
import com.ectrpg.view.dialog.obj.Case;
import com.ectrpg.view.dialog.obj.TimerText;
import org.frice.obj.AttachedAbstractObjects;
import org.frice.obj.AttachedObjects;
import org.frice.obj.button.FText;
import org.frice.obj.button.SimpleText;
import org.frice.resource.graphics.ColorResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TalkingDialog implements IDialog, Serializable, IManager {
    //Size of a talking dialog: 480 * 160
    public static final int POSITION_X = 64;
    public static final int POSITION_Y = 320;
    public static final int OBJECTS_X = 10;
    public static final int OBJECTS_Y = 0;
    public static final int ROW_SPACING = 0;
    public static final int LINE_HEIGHT = 35;
    public static final int TEXT_SIZE = 24;
    private transient int seleTimer = 0;
    private transient AttachedObjects bottom;
    private transient AttachedAbstractObjects dialog;
    private transient boolean ended = false;
    private transient Case selected;
    private final List<FText> textObj;
    private final List<TimerText> tt;
    private final List<Case> cases;

    public TalkingDialog(@Nullable List<String> s, @NotNull List<Case> cases) {
        // FIXME: 2018/7/17 0017 Init in init(), not here.
        this.cases = cases;
        for (Case c : this.cases) {
            c.setGroup(this);
        }
        if (s != null && !s.isEmpty()) {
            this.tt = new ArrayList<>();
            this.textObj = new ArrayList<>();
            for (String str : s) {
                SimpleText st = new SimpleText(ColorResource.WHITE, "", TalkingDialog.POSITION_X + TalkingDialog.OBJECTS_X,
                        TalkingDialog.POSITION_Y + TalkingDialog.OBJECTS_Y + TalkingDialog.LINE_HEIGHT * (this.tt.size() + 1));
                st.setTextSize(TEXT_SIZE);
                st.setFontName(Localization.getFont());
                this.textObj.add(st);
                this.tt.add(new TimerText(str, textObj.get(textObj.size() - 1)));
            }
            for (int i = 0; i < this.tt.size() - 1; i++) {
                this.tt.get(i).init(this.tt.get(i + 1));
                this.tt.get(i).setGroup(this);
            }
            this.tt.get(this.tt.size() - 1).init(cases.get(0));
            this.tt.get(this.tt.size() - 1).setGroup(this);
        } else {
            this.tt = null;
            this.textObj = null;
        }
    }

    @Override
    public AttachedAbstractObjects getTopAttachedObjects() {
        if (dialog != null) {
            return dialog;
        }
        this.dialog = new AttachedAbstractObjects(new ArrayList<>());
        for (Case c : cases) {
            dialog.addObject(c.getTopObject());
        }
        if (tt != null) {
            for (FText st : textObj) {
                dialog.addObject(st);
            }
        }
        return dialog;
    }

    @Override
    public AttachedObjects getBottomAttachedObjects() {
        if (bottom != null) {
            return bottom;
        }
        this.bottom = new AttachedObjects(new ArrayList<>());
        for (Case c : cases) {
            bottom.addObject(c.getBottomObject());
        }
        return bottom;
    }

    @Override
    public int getResource() {
        return 0;
    }

    @Override
    public double getX() {
        return POSITION_X;
    }

    @Override
    public double getY() {
        return POSITION_Y;
    }

    @Override
    public void onRefresh() {
        if (seleTimer != 0) {
            seleTimer--;
        }
        for (Case c : cases) {
            c.onRefresh();
        }
        if (tt != null) {
            for (TimerText t : tt) {
                t.onRefresh();
            }
        }
        if (ended) {
            if (Keyboard.isKeyPressed(Keyboard.KEY_UP, Keyboard.STATE_DIALOG) && seleTimer == 0) {
                Case willSelected = this.selected.getBefore();
                if (willSelected != null) {
                    this.selected.setSelected(false);
                    this.selected = willSelected;
                    this.selected.setSelected(true);
                } else {
                    //TODO Player pressed a useless key
                }
                seleTimer = 20;
            }
            if (Keyboard.isKeyPressed(Keyboard.KEY_DOWN, Keyboard.STATE_DIALOG) && seleTimer == 0) {
                Case willSelected = this.selected.getNext();
                if (willSelected != null) {
                    this.selected.setSelected(false);
                    this.selected = willSelected;
                    this.selected.setSelected(true);
                } else {
                    //TODO Player pressed a useless key
                }
                seleTimer = 20;
            }
            if (Keyboard.isKeyPressed(Keyboard.KEY_USE, Keyboard.STATE_DIALOG)) {
                this.selected.active();
                GameFrame.getInstance().unRegisiterDialog(this);
            }
        }
    }

    @Override
    public void end() {
        this.selected = cases.get(0);
        this.selected.setSelected(true);
        this.ended = true;
    }

    @Override
    public void startCome() {
        if (tt == null) {
            cases.get(0).startCome();
        } else {
            tt.get(0).startCome();
        }
    }
}
