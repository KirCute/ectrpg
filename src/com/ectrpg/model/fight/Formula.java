package com.ectrpg.model.fight;

import com.ectrpg.model.LocationPair;
import com.ectrpg.model.entity.HeartEntity;
import com.ectrpg.controller.LambdaFactory;
import com.ectrpg.model.Regisiterable;
import org.frice.obj.SideEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Formula implements Regisiterable, Cloneable {
    private double moveRad;
    private double rad;
    private int size;
    private float speed;
    private final int formulaLevel;
    private int roundSpeed;
    private int resourceType;
    private int resource;
    private HeartEntity owner;
    private LocationPair<Float> location;

    public Formula(@Nullable HeartEntity owner, int formulaLevel, int resourceType, int resource, int size,
                   @NotNull LocationPair<Float> location) {
        this.formulaLevel = formulaLevel;
        this.owner = owner;
        this.resourceType = resourceType;
        this.resource = resource;
        this.location = location;
        this.size = size;
    }

    public Bullet makeBullet(int resourceType, int resource, int damage, @NotNull LocationPair<Float> location,
                             @NotNull SideEffect active, @Nullable SideEffect onRegisiter, @Nullable SideEffect onUnRegisiter) {
        return LambdaFactory.bulletFactory(owner, resourceType, resource, damage, location, active, onRegisiter, onUnRegisiter);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getFormulaLevel() {
        return formulaLevel;
    }

    public void addMoveRad(double rad) {
        this.moveRad += rad;
        this.toTrueRad();
    }

    public void addRoundSpeed(int rad) {
        this.roundSpeed += rad;
    }

    public void addRad(double rad) {
        this.rad += rad;
        this.toTrueRad();
    }

    public LocationPair<Float> getLocation() {
        return location;
    }

    public void setLocation(float x, float y) {
        this.location.setX(x);
        this.location.setY(y);
    }

    private void toTrueRad() {
        this.rad %= 2 * Math.PI;
        this.moveRad %= 2 * Math.PI;
    }

    public double getMoveRad() {
        return moveRad;
    }

    public void setMoveRad(double moveRad) {
        this.moveRad = moveRad;
    }

    public double getRad() {
        return rad;
    }

    public void setRad(double rad) {
        this.rad = rad;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getRoundSpeed() {
        return roundSpeed;
    }

    public void setRoundSpeed(int roundSpeed) {
        this.roundSpeed = roundSpeed;
    }

    public int getResourceType() {
        return resourceType;
    }

    public void setResourceType(int resourceType) {
        this.resourceType = resourceType;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public HeartEntity getOwner() {
        return owner;
    }

    public void setOwner(HeartEntity owner) {
        this.owner = owner;
    }

    public void move() {
        if (speed == 0) {
            return;
        }
        double radInQuad = this.moveRad * 4 % Math.PI;
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

    public void changeView() {
        resource += roundSpeed;
        if (resource >= (120 / formulaLevel)) {
            resource %= 120 / formulaLevel;
        }
    }

    private void setLocationByLP(LocationPair<Float> location) {
        this.location = location;
    }

    public abstract void active();
}
