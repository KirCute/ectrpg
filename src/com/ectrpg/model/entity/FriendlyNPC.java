package com.ectrpg.model.entity;

import com.ectrpg.controller.service.Resource;
import com.ectrpg.model.LocationPair;

public class FriendlyNPC extends Entity {
    private LocationPair<Float> spawn;
    private int liveArea;
    private float keepTime = 0F;

    public FriendlyNPC(LocationPair<Float> location, int toward, String name, int liveArea) {
        super(location, toward, name);
        this.spawn = location;
        this.liveArea = liveArea;
    }

    @Override
    public void action() {
        if (Resource.getRandom().nextInt(100) < 110.0f - keepTime) {
            keepTime += .2f;
        } else {
            if (this.getMoving() != NOTGO) {
                this.setWonderMoving(NOTGO);
            }
            else {
                this.setWonderMoving(Resource.getRandom().nextInt(8) + 1);
                this.changeTowardFromMoving();
            }
            keepTime = .0f;
        }
    }

    @Override
    public void onUse() {
        // TODO: 2018/7/15 0015 Player greet 
    }
    
    @Override
    public void onRegisiter() {

    }

    @Override
    public void onUnRegisiter() {

    }
}
