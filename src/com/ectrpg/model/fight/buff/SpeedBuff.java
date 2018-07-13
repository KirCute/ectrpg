package com.ectrpg.model.fight.buff;

import com.ectrpg.model.entity.HeartEntity;

public class SpeedBuff extends Buff {
    private final float times;
    public SpeedBuff(int tick, float times) {
        super("Speed", tick);
        this.times = times;
    }

    @Override
    public void onRegisiter(HeartEntity e) {
        e.setNormalSpeed(e.getNormalSpeed() * times);
    }

    @Override
    public void onUnRegisiter(HeartEntity e) {
        e.setNormalSpeed(e.getNormalSpeed() / times);
    }

    @Override
    public void active(HeartEntity entity) {

    }
}
