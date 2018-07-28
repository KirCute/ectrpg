package com.ectrpg.view.dialog;

import com.ectrpg.controller.Keyboard;
import com.ectrpg.db.Localization;
import com.ectrpg.view.GameFrame;
import com.ectrpg.view.ResourcesManager;
import com.ectrpg.view.dialog.obj.TimerText;
import org.frice.obj.AttachedAbstractObjects;
import org.frice.obj.AttachedObjects;
import org.frice.obj.SideEffect;
import org.frice.obj.button.FText;
import org.frice.obj.button.SimpleText;
import org.frice.obj.sub.ImageObject;
import org.frice.resource.graphics.ColorResource;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SimpleTalkingDialog implements IDialog, Seed, IManager {
    public static final int NEXT_POSITION_X = 440;
    public static final int NEXT_POSITION_Y = 130;
    private transient int timer = 0;
    private transient int res = 0;
    private transient int direction = 1;
    private transient AttachedObjects bottom;
    private transient AttachedAbstractObjects dialog;
    private transient List<TimerText> tt;
    private transient List<FText> textObj;
    private transient ImageObject next;
    private final SideEffect nextAction;
	private final List<String> s;

    public SimpleTalkingDialog(@NotNull List<String> s, @NotNull SideEffect nextAction) {
        this.s = s;
        this.nextAction = nextAction;
    }
	
	@Override
	public void init() {
		this.tt = new ArrayList<>();
        this.textObj = new ArrayList<>();
        for (String str : s) {
            SimpleText st = new SimpleText(ColorResource.WHITE, "", TalkingDialog.POSITION_X + TalkingDialog.OBJECTS_X,
                    TalkingDialog.POSITION_Y + TalkingDialog.OBJECTS_Y + TalkingDialog.LINE_HEIGHT * (this.tt.size() + 1));
            st.setTextSize(TalkingDialog.TEXT_SIZE);
            st.setFontName(Localization.getFont());
            this.textObj.add(st);
            this.tt.add(new TimerText(str, textObj.get(textObj.size() - 1)));
        }
        for (int i = 0; i < this.tt.size() - 1; i++) {
            this.tt.get(i).init(this.tt.get(i + 1));
            this.tt.get(i).setGroup(this);
        }
        this.tt.get(this.tt.size() - 1).setGroup(this);
        this.next = new ImageObject(ResourcesManager.get(2).part(0, 0, 17, 17), TalkingDialog.POSITION_X + NEXT_POSITION_X,
                TalkingDialog.POSITION_Y + NEXT_POSITION_Y);
	}

    @Override
    public void end() {
        this.res = 1;
        this.next.setRes(ResourcesManager.get(2).part(0, res * 17, 17, 17));
    }

    @Override
    public AttachedAbstractObjects getTopAttachedObjects() {
        if (this.dialog != null) {
            return dialog;
        }
        this.dialog = new AttachedAbstractObjects(new ArrayList<>());
        for (FText ft : textObj) {
            this.dialog.addObject(ft);
        }
        return dialog;
    }

    @Override
    public AttachedObjects getBottomAttachedObjects() {
        if (this.bottom != null) {
            return bottom;
        }
        this.bottom = new AttachedObjects(new ArrayList<>());
        this.bottom.addObject(next);
        return bottom;
    }

    @Override
    public int getResource() {
        return 0;
    }

    @Override
    public double getX() {
        return TalkingDialog.POSITION_X;
    }

    @Override
    public double getY() {
        return TalkingDialog.POSITION_Y;
    }

    @Override
    public void onRefresh() {
        if (timer > 0) {
            timer--;
        }
        for (TimerText t : tt) {
            t.onRefresh();
        }
        if (res > 0 && timer == 0) {
            res += direction;
            if (res == 1) {
                direction = 1;
            }
            if (res == 8) {
                direction = -1;
            }
            this.next.setRes(ResourcesManager.get(2).part(0, res * 17, 17, 17));
            timer = 5;
            if (Keyboard.isKeyPressed(Keyboard.KEY_USE, Keyboard.STATE_DIALOG)) {
                GameFrame.getInstance().unRegisiterDialog(this);
                nextAction.invoke();
            }
        }
    }

    @Override
    public void startCome() {
        tt.get(0).startCome();
    }
}

