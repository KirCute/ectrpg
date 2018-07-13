package com.ectrpg.event;

import com.ectrpg.model.LocationPair;
import com.ectrpg.model.Pair;

import java.util.EventObject;

public final class PlayerInDoorEvent extends EventObject {
    private final Pair<Integer, LocationPair<Float>> teleport;
    private final int toward;

    public PlayerInDoorEvent(Object source, Pair<Integer, LocationPair<Float>> teleport, int toward) {
        super(source);
        this.teleport = teleport;
        this.toward = toward;
    }

    public Pair<Integer, LocationPair<Float>> getTeleport() {
        return teleport;
    }

    public int getToward() {
        return toward;
    }
}
