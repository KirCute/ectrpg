package com.ectrpg.model;

import java.io.Serializable;

public class LocationPair<T extends Number> extends Pair<T, T> implements Serializable {
    private static long serialVersionUID = 1010991160108112114L;

    public LocationPair(T x, T y) {
        super(x, y);
    }

    public LocationPair() {
        super();
    }

    public LocationPair<Integer> toIntegerPair() {
        return new LocationPair<>(
                (this.getX().floatValue() > this.getX().intValue() ? this.getX().intValue() : this.getX().intValue() - 1),
                (this.getY().floatValue() > this.getY().intValue() ? this.getY().intValue() : this.getY().intValue() - 1)
        );
    }

    public LocationPair<Float> toFloatPair() {
        return new LocationPair<>(this.getX().floatValue(), this.getY().floatValue());
    }

    @Override
    public LocationPair<T> changeX(T t) {
        return new LocationPair<>(t, this.getY());
    }

    @Override
    public LocationPair<T> changeY(T t) {
        return new LocationPair<>(this.getX(), t);
    }
}
