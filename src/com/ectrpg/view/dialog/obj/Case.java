package com.ectrpg.view.dialog.obj;

import com.ectrpg.db.Localization;
import com.ectrpg.view.ResourcesManager;
import com.ectrpg.view.dialog.IManager;
import com.ectrpg.view.dialog.IText;
import com.ectrpg.view.dialog.TalkingDialog;
import org.frice.obj.FObject;
import org.frice.obj.SideEffect;
import org.frice.obj.button.FText;
import org.frice.obj.button.SimpleText;
import org.frice.obj.sub.ImageObject;
import org.frice.resource.graphics.ColorResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

public class Case implements IText, Serializable {
    public static final int FRAME_INSET = 9;
    public static final int CASE_INSET = 3;

    private final TimerText text;
    private Case before;
    private Case next;
    private final SideEffect active;
    private final ImageObject table = new ImageObject(ResourcesManager.get(1).part(0, 0, 460, 35));
    private final FText textObj;

    public Case(@NotNull String text, int line, @NotNull SideEffect active) {
        this.active = active;
        this.textObj = new SimpleText(ColorResource.WHITE, "", TalkingDialog.POSITION_X + TalkingDialog.OBJECTS_X + CASE_INSET,
                TalkingDialog.POSITION_Y + TalkingDialog.OBJECTS_Y + line * TalkingDialog.LINE_HEIGHT);
        this.textObj.setTextSize(TalkingDialog.TEXT_SIZE);
        this.textObj.setFontName(Localization.getFont());
        this.text = new TimerText(text, textObj);
        this.table.setX(TalkingDialog.POSITION_X + TalkingDialog.OBJECTS_X);
        this.table.setY(TalkingDialog.POSITION_Y + TalkingDialog.OBJECTS_Y + TalkingDialog.LINE_HEIGHT * (line - 1) + TalkingDialog.ROW_SPACING * line + FRAME_INSET);
    }

    public FText getTopObject() {
        return textObj;
    }

    public FObject getBottomObject() {
        return table;
    }

    public void init(@Nullable IText nextCome, @Nullable Case before, @Nullable Case next) {
        this.before = before;
        this.next = next;
        this.text.init(nextCome);
    }

    public void setGroup(IManager group) {
        this.text.setGroup(group);
    }

    public void startCome() {
        text.startCome();
    }

    public void onRefresh() {
        text.onRefresh();
    }

    public void setSelected(boolean sele) {
        if (sele) {
            this.table.setRes(ResourcesManager.get(1).part(0, 35, 460, 35));
        } else {
            this.table.setRes(ResourcesManager.get(1).part(0, 0, 460, 35));
        }
    }

    public Case getBefore() {
        return before;
    }

    public Case getNext() {
        return next;
    }

    public void active() {
        this.active.invoke();
    }
}
