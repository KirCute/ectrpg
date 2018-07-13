package com.ectrpg.model.entity;

import com.ectrpg.model.LocationPair;

public abstract class FriendlyNPC extends Entity {
    private LocationPair<Float> spawn;
    private int liveArea;

    public FriendlyNPC(LocationPair<Float> location, int toward, String name, int liveArea) {
        super(location, toward, "???");
        this.spawn = location;
        this.liveArea = liveArea;
    }

    @Override
    public void active() {
        //TODO Friendly NPC Active
    }

    @Override
    public void onRegisiter() {

    }

    @Override
    public void onUnRegisiter() {

    }
}
