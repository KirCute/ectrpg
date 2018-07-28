package com.ectrpg.model.entity;

import com.ectrpg.controller.Keyboard;
import com.ectrpg.model.LocationPair;
import com.ectrpg.model.fight.Bullet;
import com.ectrpg.model.fight.buff.Buff;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

public final class Player extends HeartEntity implements Serializable {
    private static final long serialVersionUID = 1010991160112108114L;
    public static final LocationPair<Float> DEFAULT_PLAYER_LOCATION = new LocationPair<>(9F, 7F);
    private int energy;
    private int strength;

    @Override
    public void onUse() {
    }

    @Override
    public void onMiss(Bullet bullet) {
        //TODO Player Miss
    }

    @Override
    public void onDeath(HeartEntity killer) {
        //TODO Player Die
    }

    @Override
    public void onGraze(Bullet bullet) {
        //TODO Player Graze
    }

    @Override
    public void onFailed() {
        //TODO Failed
    }

    @Override
    public void setMovingWay(int movingWay) {
        if (movingWay == MOVING_FASTER && strength < 648000) {
            return;
        }
        super.setMovingWay(movingWay);
    }

    @Override
    public void action() {
        if (--energy <= 0) {
            // TODO: 2018/7/17 0017 Player die of tiredness
        }
        if (strength < energy - 360) {
            strength += 360;
        } else {
            strength = energy;
        }
        if (this.getMovingWay() == MOVING_FASTER) {
            strength -= 720;
            if (this.strength < 216000) {
                this.setMovingWay(MOVING_NORMAL);
            }
        } else if (this.getMovingWay() == MOVING_NORMAL) {
            strength -= 180;
        }
        Keyboard.setChange(false);
        this.setMovingWay((Keyboard.isKeyPressed(Keyboard.KEY_FAST, Keyboard.STATE_MOVING) ? Entity.MOVING_FASTER : Entity.MOVING_NORMAL));
        if ((this.getToward() == Entity.TOWARD_UP) || (this.getToward() == Entity.TOWARD_DOWN)) {
            if (Keyboard.isKeyPressed(Keyboard.KEY_UP, Keyboard.STATE_MOVING) && !Keyboard.isKeyPressed(Keyboard.KEY_DOWN, Keyboard.STATE_MOVING)) {
                this.setToward(Entity.TOWARD_UP);
                if (Keyboard.isKeyPressed(Keyboard.KEY_LEFT, Keyboard.STATE_MOVING) && !Keyboard.isKeyPressed(Keyboard.KEY_RIGHT, Keyboard.STATE_MOVING)) {
                    this.setWonderMoving(Entity.GOTOWARD_UPLEFT);
                } else if (!Keyboard.isKeyPressed(Keyboard.KEY_LEFT, Keyboard.STATE_MOVING) && Keyboard.isKeyPressed(Keyboard.KEY_RIGHT, Keyboard.STATE_MOVING)) {
                    this.setWonderMoving(Entity.GOTOWARD_UPRIGHT);
                } else {
                    this.setWonderMoving(Entity.GOTOWARD_UP);
                }
            } else if (!Keyboard.isKeyPressed(Keyboard.KEY_UP, Keyboard.STATE_MOVING) && Keyboard.isKeyPressed(Keyboard.KEY_DOWN, Keyboard.STATE_MOVING)) {
                this.setToward(Entity.TOWARD_DOWN);
                if (Keyboard.isKeyPressed(Keyboard.KEY_LEFT, Keyboard.STATE_MOVING) && !Keyboard.isKeyPressed(Keyboard.KEY_RIGHT, Keyboard.STATE_MOVING)) {
                    this.setWonderMoving(Entity.GOTOWARD_DOWNLEFT);
                } else if (!Keyboard.isKeyPressed(Keyboard.KEY_LEFT, Keyboard.STATE_MOVING) && Keyboard.isKeyPressed(Keyboard.KEY_RIGHT, Keyboard.STATE_MOVING)) {
                    this.setWonderMoving(Entity.GOTOWARD_DOWNRIGHT);
                } else {
                    this.setWonderMoving(Entity.GOTOWARD_DOWN);
                }
            } else if (Keyboard.isKeyPressed(Keyboard.KEY_LEFT, Keyboard.STATE_MOVING) && !Keyboard.isKeyPressed(Keyboard.KEY_RIGHT, Keyboard.STATE_MOVING)) {
                this.setToward(Entity.TOWARD_LEFT);
                this.setWonderMoving(Entity.GOTOWARD_LEFT);
            } else if (!Keyboard.isKeyPressed(Keyboard.KEY_LEFT, Keyboard.STATE_MOVING) && Keyboard.isKeyPressed(Keyboard.KEY_RIGHT, Keyboard.STATE_MOVING)) {
                this.setToward(Entity.TOWARD_LEFT);
                this.setWonderMoving(Entity.GOTOWARD_RIGHT);
            } else {
                this.setWonderMoving(Entity.NOTGO);
            }
        } else if ((this.getToward() == Entity.TOWARD_LEFT) || (this.getToward() == Entity.TOWARD_RIGHT)) {
            if (Keyboard.isKeyPressed(Keyboard.KEY_LEFT, Keyboard.STATE_MOVING) && !Keyboard.isKeyPressed(Keyboard.KEY_RIGHT, Keyboard.STATE_MOVING)) {
                this.setToward(Entity.TOWARD_LEFT);
                if (Keyboard.isKeyPressed(Keyboard.KEY_UP, Keyboard.STATE_MOVING) && !Keyboard.isKeyPressed(Keyboard.KEY_DOWN, Keyboard.STATE_MOVING)) {
                    this.setWonderMoving(Entity.GOTOWARD_UPLEFT);
                } else if (!Keyboard.isKeyPressed(Keyboard.KEY_UP, Keyboard.STATE_MOVING) && Keyboard.isKeyPressed(Keyboard.KEY_DOWN, Keyboard.STATE_MOVING)) {
                    this.setWonderMoving(Entity.GOTOWARD_DOWNLEFT);
                } else {
                    this.setWonderMoving(Entity.GOTOWARD_LEFT);
                }
            } else if (!Keyboard.isKeyPressed(Keyboard.KEY_LEFT, Keyboard.STATE_MOVING) && Keyboard.isKeyPressed(Keyboard.KEY_RIGHT, Keyboard.STATE_MOVING)) {
                this.setToward(Entity.TOWARD_RIGHT);
                if (Keyboard.isKeyPressed(Keyboard.KEY_UP, Keyboard.STATE_MOVING) && !Keyboard.isKeyPressed(Keyboard.KEY_DOWN, Keyboard.STATE_MOVING)) {
                    this.setWonderMoving(Entity.GOTOWARD_UPRIGHT);
                } else if (!Keyboard.isKeyPressed(Keyboard.KEY_UP, Keyboard.STATE_MOVING) && Keyboard.isKeyPressed(Keyboard.KEY_DOWN, Keyboard.STATE_MOVING)) {
                    this.setWonderMoving(Entity.GOTOWARD_DOWNRIGHT);
                } else {
                    this.setWonderMoving(Entity.GOTOWARD_RIGHT);
                }
            } else if (Keyboard.isKeyPressed(Keyboard.KEY_UP, Keyboard.STATE_MOVING) && !Keyboard.isKeyPressed(Keyboard.KEY_DOWN, Keyboard.STATE_MOVING)) {
                this.setToward(Entity.TOWARD_UP);
                this.setWonderMoving(Entity.GOTOWARD_UP);
            } else if (!Keyboard.isKeyPressed(Keyboard.KEY_UP, Keyboard.STATE_MOVING) && Keyboard.isKeyPressed(Keyboard.KEY_DOWN, Keyboard.STATE_MOVING)) {
                this.setToward(Entity.TOWARD_DOWN);
                this.setWonderMoving(Entity.GOTOWARD_DOWN);
            } else {
                this.setWonderMoving(Entity.NOTGO);
            }
        }
        Keyboard.setChange(true);
    }

    public Player(LocationPair<Float> location, int toward, String name, int heart, byte lives, @NotNull List<Buff> buffs) {
        super(location, toward, name, heart, lives, buffs);
        this.energy = 6480000;
        this.strength = energy;
    }
}
