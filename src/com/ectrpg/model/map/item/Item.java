package com.ectrpg.model.map.item;

import com.ectrpg.controller.service.Resource;
import com.ectrpg.model.LocationPair;
import com.ectrpg.model.Regisiterable;
import com.ectrpg.model.Useable;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public abstract class Item implements Serializable, Useable, Regisiterable {
    private static final long serialVersionUID = 1010991160105116109L;

    private final String name;
    private final LocationPair<Integer> location;
    private int usingView;

    public LocationPair<Integer> getLocation() {
        return location;
    }

    public void setUsingView(int view) {
        this.usingView = view;
    }

    public int getUsingView() {
        return usingView;
    }

    public String getName() {
        return name;
    }

    public Item(@NotNull String name, @NotNull LocationPair<Integer> location) {
        this.location = location;
        this.name = name;
    }

    public boolean isRegisitered() {
        return Resource.isRegisitered(this);
    }

    public int getId() {
        return Resource.getId(this);
    }

    public abstract void onUse();

    @Override
    public void onUnRegisiter() {

    }

    @Override
    public void onRegisiter() {
        
    }
}
