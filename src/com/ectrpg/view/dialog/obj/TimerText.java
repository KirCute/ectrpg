package com.ectrpg.view.dialog.obj;

import com.ectrpg.controller.Keyboard;
import com.ectrpg.db.Localization;
import com.ectrpg.view.dialog.IManager;
import com.ectrpg.view.dialog.IText;
import org.frice.obj.button.FText;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TimerText implements IText {
    private final String text;
    private IText nextCome;
    private final FText textObj;
    private IManager group;
    private int textProgress = 0;
    private int timer;
    private boolean end = false;

    public TimerText(@NotNull String text, @NotNull FText textObj) {
        this.text = text;
        this.textObj = textObj;
    }

    public void init(@Nullable IText nextCome) {
        this.nextCome = nextCome;
    }

    public void setGroup(IManager group) {
        this.group = group;
    }

    public void startCome() {
        end = false;
        textObj.setText("");
        textProgress = 1;
    }

    public void onRefresh() {
        for (int i = 0; i < 2 / Localization.getWidth(); i++) {
            if (!end && textProgress > 0 && textProgress <= text.length()) {
                timer++;
                if (timer >= 2 || (Keyboard.isKeyPressed(Keyboard.KEY_BACK, Keyboard.STATE_DIALOG) && timer >= 1)) {
                    textObj.setText(text.substring(0, textProgress));
                    textProgress++;
                    timer = 0;
                }
            } else if (!end && textProgress > text.length()) {
                end = true;
                textProgress = 0;
                timer = 0;
                if (nextCome == null) {
                    group.end();
                } else {
                    nextCome.startCome();
                }
            }
        }
    }
}
