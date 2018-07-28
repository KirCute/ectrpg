package com.ectrpg.model.map.item;

import com.ectrpg.event.EventManager;
import com.ectrpg.model.LocationPair;
import com.ectrpg.model.Pair;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class Door extends Item implements Serializable {
    private static final long serialVersionUID = 1010991160100111114L;
    private final int toward;
    private Pair<Integer, LocationPair<Float>> teleport;

    public Pair<Integer, LocationPair<Float>> getTeleport() {
        return teleport;
    }

    public Door(@NotNull String name, @NotNull LocationPair<Integer> location, @NotNull Pair<Integer, LocationPair<Float>> teleport, int toward) {
        super(name, location);
        this.teleport = teleport;
        this.toward = toward;
    }

    @Override
    public void onUse() {
        this.setUsingView(1);
        EventManager.getInstance().performedPd(this, this.getTeleport(), toward);
        System.out.println("Door.onUse()");
        this.setUsingView(0);
    }
}
