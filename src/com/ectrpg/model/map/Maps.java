package com.ectrpg.model.map;

import com.ectrpg.model.LocationPair;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.BitSet;

public class Maps implements Serializable {
    private static final long serialVersionUID = 1010991160109097112L;
    private final int mapId;
    private final int width;
    private final int brightness;
    private final LocationPair<Integer> viewShifting;
    private final BitSet bs;

    public Maps(int mapId, int width, int brightness, @NotNull BitSet bs, @NotNull LocationPair<Integer> viewShifting) {
        this.mapId = mapId;
        this.width = width;
        this.bs = bs;
        this.brightness = brightness;
        this.viewShifting = viewShifting;
    }

    public int getBrightness() {
        return brightness;
    }

    public boolean isBlock(int x, int y) {
        return bs.get(y * width + x);
    }

    public boolean isBlock(LocationPair<Integer> location) {
        return this.isBlock(location.getX(), location.getY());
    }

    public int getMapHeight() {
        return bs.length() / width;
    }

    public int getMapWidth() {
        return width;
    }

    public LocationPair<Integer> getViewShifting() {
        return viewShifting;
    }

    public int getMapId() {
        return mapId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Maps maps = (Maps) o;
        return width == maps.width &&
                brightness == maps.brightness &&
                Arrays.equals(bs.toLongArray(), maps.bs.toLongArray());
    }

    @Override
    public int hashCode() {
        int result = width;
        result = 31 * result + bs.hashCode();
        result = 31 * result + brightness;
        return result;
    }
}