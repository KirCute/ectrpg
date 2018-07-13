package com.ectrpg.model.fight.buff;

import com.ectrpg.model.entity.HeartEntity;

public abstract class Buff {
    private final String name;
    private byte timeTick = 0;
    private byte timeSec = 0;
    private byte timeMin = 0;
    private byte timeHour = 0;

    public Buff(String name, int tick) {
        this.name = name;
        if (tick <= -1) {
            this.timeTick = -1;
        } else {
            this.timeTick = (byte) (tick % 120);
            this.timeSec = (byte) (tick / 120 % 60);
            this.timeMin = (byte) (tick / 7200 % 60);
            this.timeHour = (byte) (tick / 432000);
        }
    }

    private void refreshTime() {
        if (timeTick <= -1) {
            return;
        }
        if (--timeTick == -1) {
            timeTick = 120;
            --timeSec;
        }
        if (timeSec <= -1) {
            timeSec = 59;
            --timeMin;
        }
        if (timeMin <= -1) {
            timeMin = 59;
            --timeHour;
        }
    }

    public void onRefresh(HeartEntity e) {
        refreshTime();
        if (timeHour <= -1) {
            this.onUnRegisiter(e);
            e.removeBuff(this);
        } else {
            this.active(e);
        }
    }

    public abstract void onRegisiter(HeartEntity e);

    public abstract void onUnRegisiter(HeartEntity e);

    public abstract void active(HeartEntity e);
}
