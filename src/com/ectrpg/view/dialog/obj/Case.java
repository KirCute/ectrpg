package com.ectrpg.view.dialog.obj;

import com.ectrpg.db.Localization;
import com.ectrpg.view.ResourcesManager;
import com.ectrpg.view.dialog.IManager;
import com.ectrpg.view.dialog.IText;
import com.ectrpg.view.dialog.Seed;
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

public class Case implements IText, Seed {
    public static final int FRAME_INSET = 9;
    public static final int CASE_INSET = 3;

	private final String text;
	private final int line;
	private Case nextCome;
    private Case before;
    private Case next;
    private final SideEffect active;
    private transient TimerText timerText;
    private transient ImageObject table = new ImageObject(ResourcesManager.get(1).part(0, 0, 460, 35));
    private transient FText textObj;

    public Case(@NotNull String text, int line, @NotNull SideEffect active) {
		this.text = text;
		this.line = line;
        this.active = active;
    }

    public void setRelation(@Nullable Case nextCome, @Nullable Case before, @Nullable Case next) {
        this.nextCome = nextCome;
        this.before = before;
        this.next = next;
    }

	@Override
	public void init() {
		this.textObj = new SimpleText(ColorResource.WHITE, "", TalkingDialog.POSITION_X + TalkingDialog.OBJECTS_X + CASE_INSET,
                TalkingDialog.POSITION_Y + TalkingDialog.OBJECTS_Y + line * TalkingDialog.LINE_HEIGHT);
        this.textObj.setTextSize(TalkingDialog.TEXT_SIZE);
        this.textObj.setFontName(Localization.getFont());
        this.timerText = new TimerText(Localization.query(text), textObj);
        this.table.setX(TalkingDialog.POSITION_X + TalkingDialog.OBJECTS_X);
        this.table.setY(TalkingDialog.POSITION_Y + TalkingDialog.OBJECTS_Y + TalkingDialog.LINE_HEIGHT * (line - 1) + TalkingDialog.ROW_SPACING * line + FRAME_INSET);
        this.timerText.init(nextCome);
	}
	
    public FText getTopObject() {
        return textObj;
    }

    public FObject getBottomObject() {
        return table;
    }

    public void setGroup(IManager group) {
        this.timerText.setGroup(group);
    }

    public void startCome() {
        this.timerText.startCome();
    }

    public void onRefresh() {
        this.timerText.onRefresh();
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
