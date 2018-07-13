package com.ectrpg.model;

import java.io.Serializable;

public class Pair<X, Y> implements Serializable {
    private static long serialVersionUID = 1010991160112097114L;
    private X x;
    private Y y;

    public Pair(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    public Pair() {
        this.x = null;
        this.y = null;
    }

    public X getX() {
        return x;
    }

    public void setX(X x) {
        this.x = x;
    }

    public Y getY() {
        return y;
    }

    public void setY(Y y) {
        this.y = y;
    }

    public Pair changeX(X x) {
        return new Pair<>(x, this.y);
    }

    public Pair changeY(Y y) {
        return new Pair<>(this.x, y);
    }

    public void setXY(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    public Pair<String, String> toStringPair() {
        return new Pair<>(x.toString(), y.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return (x != null ? x.equals(pair.x) : pair.x == null) && (y != null ? y.equals(pair.y) : pair.y == null);
    }

    @Override
    public int hashCode() {
        int result = x != null ? x.hashCode() : 0;
        result = 31 * result + (y != null ? y.hashCode() : 0);
        return result;
    }
}
