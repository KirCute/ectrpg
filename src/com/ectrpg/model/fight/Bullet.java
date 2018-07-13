package com.ectrpg.model.fight;

import com.ectrpg.model.LocationPair;
import com.ectrpg.model.Regisiterable;
import com.ectrpg.model.entity.HeartEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Bullet implements Regisiterable, Cloneable {
    private double rad;
    private float speed;
    private int damage;
    private int resourceType;
    private int resource;
    private HeartEntity owner;
    private LocationPair<Float> location;

    public Bullet(@Nullable HeartEntity owner, int resourceType, int resource, int damage, @NotNull LocationPair<Float> location) {
        this.owner = owner;
        this.resourceType = resourceType;
        this.resource = resource;
        this.damage = damage;
        this.location = location;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public HeartEntity getOwner() {
        return owner;
    }

    public void setOwner(@Nullable HeartEntity owner) {
        this.owner = owner;
    }

    public double getRad() {
        return rad;
    }

    public void addRad(double rad) {
        this.rad += rad;
        this.toTrueRad();
    }

    public void setRad(double rad) {
        this.rad = rad;
        this.toTrueRad();
    }

    private void toTrueRad() {
        this.rad %= 2 * Math.PI;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public LocationPair<Float> getLocation() {
        return location;
    }

    public void setLocation(float x, float y) {
        this.location.setX(x);
        this.location.setY(y);
    }

    public int getResourceType() {
        return resourceType;
    }

    public void setResource(int resourceType) {
        this.resourceType = resourceType;
    }

    public void move() {
        if (speed == 0) {
            return;
        }
        double radInQuad = this.rad * 4 % Math.PI;
        float x = (float) Math.cos(radInQuad) * speed;
        float y = (float) Math.sin(radInQuad) * speed;
        switch ((int) (this.rad * 4 / Math.PI)) {
            case 0:
                break;
            case 1:
                x = -x;
                break;
            case 2:
                x = -x;
                y = -y;
                break;
            case 3:
                y = -y;
                break;
        }
        this.setLocation(location.getX() + x, location.getY() + y);
    }

    public Bullet makeSimilar(@NotNull LocationPair<Float> location) {
        try {
            Bullet clone = (Bullet) this.clone();
            clone.setLocationByLP(location);
            return clone;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setLocationByLP(LocationPair<Float> location) {
        this.location = location;
    }

    public void hit(@NotNull HeartEntity hidEntity) {
        hidEntity.removeHeart(damage);
    }

    public void beGrazed(@NotNull HeartEntity grazer) {

    }

    public void active() {

    }

    @Override
    public void onRegisiter() {

    }

    @Override
    public void onUnRegisiter() {

    }
}
