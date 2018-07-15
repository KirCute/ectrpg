package com.ectrpg.model.entity;

import com.ectrpg.model.LocationPair;
import com.ectrpg.model.map.item.Door;
import com.ectrpg.controller.service.Resource;
import com.ectrpg.model.Regisiterable;
import com.ectrpg.model.Useable;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public abstract class Entity implements Serializable, Useable, Regisiterable {
    public static final long serialVersionUID = 1010991160101110121L;
    //MOVING WAY
    public static final int MOVING_NORMAL = 0;
    public static final int MOVING_FASTER = 1;

    //TOWARD
    public static final int TOWARD_UP = 1;
    public static final int TOWARD_DOWN = 2;
    public static final int TOWARD_LEFT = 3;
    public static final int TOWARD_RIGHT = 4;

    //MOVING
    public static final int NOTGO = 0;
    public static final int GOTOWARD_UP = 1;
    public static final int GOTOWARD_DOWN = 2;
    public static final int GOTOWARD_LEFT = 3;
    public static final int GOTOWARD_RIGHT = 4;
    public static final int GOTOWARD_UPLEFT = 5;
    public static final int GOTOWARD_UPRIGHT = 6;
    public static final int GOTOWARD_DOWNLEFT = 7;
    public static final int GOTOWARD_DOWNRIGHT = 8;

    //View
    public static final int VIEW_UP = 0;
    public static final int VIEW_DOWN = 1;
    public static final int VIEW_LEFT = 2;
    public static final int VIEW_RIGHT = 3;
    public static final int VIEW_GOUPLEFT = 4;
    public static final int VIEW_GOUPRIGHT = 5;
    public static final int VIEW_GODOWNLEFT = 6;
    public static final int VIEW_GODOWNRIGHT = 7;
    public static final int VIEW_GOLEFT = 8;
    public static final int VIEW_GORIGHT = 9;

    //Side
    public static final int SIDE_UP = 1;
    public static final int SIDE_DOWN = 2;
    public static final int SIDE_LEFT = 3;
    public static final int SIDE_RIGHT = 4;
    public static final int SIDE_NEXTLEFT = -1;
    public static final int SIDE_NEXTMIDDLE = 0;
    public static final int SIDE_NEXTRIGHT = 1;

    private LocationPair<Float> location;
    private int toward;
    private int moving = NOTGO;
    private int movingWay = MOVING_NORMAL;
    private byte changeView = 0;
    private int usingView;
    private float speed = 1F;
    private float movingBuffer;
    private String name;
    private int wonderMoving;
    private boolean lastMovingSucceed;
    private boolean normalMoving = true;

    public int getToward() {
        return toward;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getWonderMoving() {
        return wonderMoving;
    }

    public void setWonderMoving(int wonderMoving) {
        this.wonderMoving = wonderMoving;
    }

    public Entity(@NotNull LocationPair<Float> location, int toward, @NotNull String name) {
        this.location = location;
        this.toward = toward;
        this.name = name;
    }

    public void setToward(int toward) {
        this.toward = toward;
    }

    public void setMoving(int moving) {
        this.moving = moving;
    }

    public void setLocation(@NotNull LocationPair<Float> location) {
        this.location = location;
    }

    public void setMovingWay(int movingWay) {
        this.movingWay = movingWay;
    }

    public void setLocation(float x, float y) {
        this.location.setXY(x, y);
    }

    public void setLocation(int x, int y) {
        this.location.setXY((float) x, (float) y);
    }

    public int getMoving() {
        return moving;
    }

    public boolean isMoving() {
        return (moving != NOTGO);
    }

    public LocationPair<Float> getLocation() {
        return location;
    }

    public boolean isIntegerBlock() {
        return (((location.getX() + 0.5F) % 1) == 0) && (((location.getY() + 0.5F) % 1) == 0);
    }

    public int getMovingWay() {
        return movingWay;
    }

    void setUsingView(int view) {
        this.usingView = view;
    }

    public int getUsingView() {
        return usingView;
    }

    public void changeViewFromMoving() {
        // TODO: 2018/7/15 0015 Two types of left or right (with different hands toward) 
        if (moving == NOTGO) {
            changeView = -1;
            usingView = toward - 1;
        } else {
            if (++changeView >= 31) {
                changeView = -32;
            }
            if (changeView >= 0) {
                switch (toward) {
                    case TOWARD_UP: usingView = VIEW_GOUPLEFT; break;
                    case TOWARD_DOWN: usingView = VIEW_GODOWNRIGHT; break;
                    case TOWARD_LEFT: usingView = VIEW_GOLEFT; break;
                    case TOWARD_RIGHT: usingView = VIEW_GORIGHT; break;
                }
            } else {
                switch (toward) {
                    case TOWARD_UP: usingView = VIEW_GOUPRIGHT; break;
                    case TOWARD_DOWN: usingView = VIEW_GODOWNLEFT; break;
                    case TOWARD_LEFT: usingView = VIEW_LEFT; break;
                    case TOWARD_RIGHT: usingView = VIEW_RIGHT; break;
                }
            }
        }
    }

    public void changeTowardFromMoving() {
        switch (wonderMoving) {
            case GOTOWARD_UP: this.setToward(TOWARD_UP); break;
            case GOTOWARD_DOWN: this.setToward(TOWARD_DOWN); break;
            case GOTOWARD_LEFT: this.setToward(TOWARD_LEFT); break;
            case GOTOWARD_RIGHT: this.setToward(TOWARD_RIGHT); break;
            case GOTOWARD_UPLEFT:
                if (this.getMoving() == GOTOWARD_UP) {
                    this.setToward(TOWARD_UP);
                } else if (this.getMoving() == GOTOWARD_LEFT) {
                    this.setToward(TOWARD_LEFT);
                } else if (this.getToward() != TOWARD_UP && this.getToward() != TOWARD_LEFT) {
                    this.setToward(Resource.getRandom().nextInt(10) > 4 ? TOWARD_UP : TOWARD_LEFT);
                }
                break;
            case GOTOWARD_UPRIGHT:
                if (this.getMoving() == GOTOWARD_UP) {
                    this.setToward(TOWARD_UP);
                } else if (this.getMoving() == GOTOWARD_RIGHT) {
                    this.setToward(TOWARD_RIGHT);
                } else if (this.getToward() != TOWARD_UP && this.getToward() != TOWARD_RIGHT) {
                    this.setToward(Resource.getRandom().nextInt(10) > 4 ? TOWARD_UP : TOWARD_RIGHT);
                }
                break;
            case GOTOWARD_DOWNLEFT:
                if (this.getMoving() == GOTOWARD_DOWN) {
                    this.setToward(TOWARD_DOWN);
                } else if (this.getMoving() == GOTOWARD_LEFT) {
                    this.setToward(TOWARD_LEFT);
                } else if (this.getToward() != TOWARD_DOWN && this.getToward() != TOWARD_LEFT) {
                    this.setToward(Resource.getRandom().nextInt(10) > 4 ? TOWARD_DOWN : TOWARD_LEFT);
                }
                break;
            case GOTOWARD_DOWNRIGHT:
                if (this.getMoving() == GOTOWARD_DOWN) {
                    this.setToward(TOWARD_DOWN);
                } else if (this.getMoving() == GOTOWARD_RIGHT) {
                    this.setToward(TOWARD_RIGHT);
                } else if (this.getToward() != TOWARD_DOWN && this.getToward() != TOWARD_RIGHT) {
                    this.setToward(Resource.getRandom().nextInt(10) > 4 ? TOWARD_DOWN : TOWARD_RIGHT);
                }
                break;
        }
    }

    public void move() {
        switch (moving) {
            case GOTOWARD_UP:
                location.setY(location.getY() - 0.03125F);
                break;
            case GOTOWARD_DOWN:
                location.setY(location.getY() + 0.03125F);
                break;
            case GOTOWARD_LEFT:
                location.setX(location.getX() - 0.03125F);
                break;
            case GOTOWARD_RIGHT:
                location.setX(location.getX() + 0.03125F);
                break;
            case GOTOWARD_UPLEFT:
                location.setX(location.getX() - 0.03125F);
                location.setY(location.getY() - 0.03125F);
                break;
            case GOTOWARD_UPRIGHT:
                location.setX(location.getX() + 0.03125F);
                location.setY(location.getY() - 0.03125F);
                break;
            case GOTOWARD_DOWNLEFT:
                location.setX(location.getX() - 0.03125F);
                location.setY(location.getY() + 0.03125F);
                break;
            case GOTOWARD_DOWNRIGHT:
                location.setX(location.getX() + 0.03125F);
                location.setY(location.getY() + 0.03125F);
                break;
        }
    }

    public boolean canEntityMove() {
        if (movingWay == MOVING_FASTER && normalMoving) {
            normalMoving = false;
            speed = speed * 2;
        } else if (movingWay == MOVING_NORMAL && !normalMoving) {
            normalMoving = true;
            speed = speed / 2;
        }
        switch (wonderMoving) {
            case GOTOWARD_UP:
                moving = wonderMoving;
                return this.canEntityMoveTo(SIDE_UP);
            case GOTOWARD_DOWN:
                moving = wonderMoving;
                return this.canEntityMoveTo(SIDE_DOWN);
            case GOTOWARD_LEFT:
                moving = wonderMoving;
                return this.canEntityMoveTo(SIDE_LEFT);
            case GOTOWARD_RIGHT:
                moving = wonderMoving;
                return this.canEntityMoveTo(SIDE_RIGHT);
            case GOTOWARD_UPLEFT:
                if (toward == TOWARD_UP) {
                    boolean master = this.canEntityMoveTo(SIDE_UP);
                    boolean slave = this.canEntityMoveTo(SIDE_LEFT);
                    if (master) {
                        if (slave) {
                            moving = GOTOWARD_UPLEFT;
                            return true;
                        } else {
                            this.moving = GOTOWARD_UP;
                            return true;
                        }
                    } else if (slave) {
                        this.moving = GOTOWARD_LEFT;
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    boolean master = this.canEntityMoveTo(SIDE_LEFT);
                    boolean slave = this.canEntityMoveTo(SIDE_UP);
                    if (master) {
                        if (slave) {
                            moving = wonderMoving;
                            return true;
                        } else {
                            this.moving = GOTOWARD_LEFT;
                            return true;
                        }
                    } else if (slave) {
                        this.moving = GOTOWARD_UP;
                        return true;
                    } else {
                        return false;
                    }
                }
            case GOTOWARD_UPRIGHT:
                if (toward == TOWARD_UP) {
                    boolean master = this.canEntityMoveTo(SIDE_UP);
                    boolean slave = this.canEntityMoveTo(SIDE_RIGHT);
                    if (master) {
                        if (slave) {
                            moving = wonderMoving;
                            return true;
                        } else {
                            this.moving = GOTOWARD_UP;
                            return true;
                        }
                    } else if (slave) {
                        this.moving = GOTOWARD_RIGHT;
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    boolean master = this.canEntityMoveTo(SIDE_RIGHT);
                    boolean slave = this.canEntityMoveTo(SIDE_UP);
                    if (master) {
                        if (slave) {
                            moving = wonderMoving;
                            return true;
                        } else {
                            this.moving = GOTOWARD_RIGHT;
                            return true;
                        }
                    } else if (slave) {
                        this.moving = GOTOWARD_UP;
                        return true;
                    } else {
                        return false;
                    }
                }
            case GOTOWARD_DOWNLEFT:
                if (toward == TOWARD_DOWN) {
                    boolean master = this.canEntityMoveTo(SIDE_DOWN);
                    boolean slave = this.canEntityMoveTo(SIDE_LEFT);
                    if (master) {
                        if (slave) {
                            moving = wonderMoving;
                            return true;
                        } else {
                            this.moving = GOTOWARD_DOWN;
                            return true;
                        }
                    } else if (slave) {
                        this.moving = GOTOWARD_LEFT;
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    boolean master = this.canEntityMoveTo(SIDE_LEFT);
                    boolean slave = this.canEntityMoveTo(SIDE_DOWN);
                    if (master) {
                        if (slave) {
                            moving = wonderMoving;
                            return true;
                        } else {
                            this.moving = GOTOWARD_LEFT;
                            return true;
                        }
                    } else if (slave) {
                        this.moving = GOTOWARD_DOWN;
                        return true;
                    } else {
                        return false;
                    }
                }
            case GOTOWARD_DOWNRIGHT:
                if (toward == TOWARD_UP) {
                    boolean master = this.canEntityMoveTo(SIDE_DOWN);
                    boolean slave = this.canEntityMoveTo(SIDE_RIGHT);
                    if (master) {
                        if (slave) {
                            moving = wonderMoving;
                            return true;
                        } else {
                            this.moving = GOTOWARD_DOWN;
                            return true;
                        }
                    } else if (slave) {
                        this.moving = GOTOWARD_RIGHT;
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    boolean master = this.canEntityMoveTo(SIDE_RIGHT);
                    boolean slave = this.canEntityMoveTo(SIDE_DOWN);
                    if (master) {
                        if (slave) {
                            moving = wonderMoving;
                            return true;
                        } else {
                            this.moving = GOTOWARD_RIGHT;
                            return true;
                        }
                    } else if (slave) {
                        this.moving = GOTOWARD_DOWN;
                        return true;
                    } else {
                        return false;
                    }
                }
            default:
                this.moving = wonderMoving;
                return false;
        }
    }

    public float getNormalSpeed() {
        return normalMoving ? speed : speed / 2F;
    }

    public void setNormalSpeed(float speed) {
        if (normalMoving) {
            this.speed = speed;
        } else {
            this.speed = speed * 2F;
        }
    }

    private LocationPair<Integer> getSideBlock(int side, int nextside) {
        switch (side) {
            case SIDE_UP:
                return new LocationPair<>((int) (location.getX() + nextside * 0.4375F), (int) (location.getY() - 0.5F));
            case SIDE_DOWN:
                return new LocationPair<>((int) (location.getX() - nextside * 0.4375F), (int) (location.getY() + 0.5F));
            case SIDE_LEFT:
                return new LocationPair<>((int) (location.getX() - 0.5F), (int) (location.getY() - nextside * 0.4375F));
            case SIDE_RIGHT:
                return new LocationPair<>((int) (location.getX() + 0.5F), (int) (location.getY() + nextside * 0.4375F));
            default:
                return null;
        }
    }

    private LocationPair<Integer> getSideBlockForEntity(int side, int nextside) {
        //TODO
        switch (side) {
            case SIDE_UP:
                return new LocationPair<>((int) (location.getX() + nextside * 0.9375F), (int) (location.getY() - 1F));
            case SIDE_DOWN:
                return new LocationPair<>((int) (location.getX() - nextside * 0.9375F), (int) (location.getY() + 1F));
            case SIDE_LEFT:
                return new LocationPair<>((int) (location.getX() - 1F), (int) (location.getY() - nextside * 0.9375F));
            case SIDE_RIGHT:
                return new LocationPair<>((int) (location.getX() + 1F), (int) (location.getY() + nextside * 0.9375F));
            default:
                return null;
        }
    }

    private boolean canEntityMoveTo(int side) {
        if ((location.getY() <= 0.5 && side == SIDE_UP) ||
                (location.getY() >= Resource.getMap().getMapHeight() - 0.5 && side == SIDE_DOWN) ||
                (location.getX() <= 0.5 && side == SIDE_LEFT) ||
                (location.getX() >= Resource.getMap().getMapWidth() - 0.5 && side == SIDE_RIGHT)) {
            return false;
        }
        if (Resource.getItem(this.getSideBlock(side, SIDE_NEXTMIDDLE)) instanceof Door) {
            if (this instanceof Player) {
                Resource.getItem(this.getSideBlock(side, SIDE_NEXTMIDDLE)).onUse();
                return true;
            } else {
                int i = this.getId();
                if (i != -1) {
                    Resource.unRegisterEntity(i);
                }
                return false;
            }
        } else {
            if (Resource.getMap().isBlock(this.getSideBlock(side, SIDE_NEXTLEFT)) ||
                    Resource.getMap().isBlock(this.getSideBlock(side, SIDE_NEXTMIDDLE)) ||
                    Resource.getMap().isBlock(this.getSideBlock(side, SIDE_NEXTRIGHT))) {
                return false;
            }
            if (Resource.getRegisteredEntities().size() > 1) {
                for (Entity e : Resource.getRegisteredEntities()) {
                    if (e == this) {
                        continue;
                    }
                    final LocationPair<Integer> integerLocationPair = e.getLocation().toIntegerPair();
                    if (integerLocationPair.equals(this.getSideBlock(side, SIDE_NEXTLEFT)) ||
                            integerLocationPair.equals(this.getSideBlock(side, SIDE_NEXTMIDDLE)) ||
                            integerLocationPair.equals(this.getSideBlock(side, SIDE_NEXTRIGHT))) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public LocationPair<Integer> toIntegerPair() {
        return new LocationPair<>(Math.round(location.getX() - 0.5F), Math.round(location.getY() - 0.5F));
    }

    public LocationPair<Integer> getFront() {
        return getSideBlock(this.toward, SIDE_NEXTMIDDLE);
    }

    public boolean isPlayer() {
        return this instanceof Player;
    }

    public boolean isRegisitered() {
        return Resource.isRegisitered(this);
    }

    public boolean isBuffered() {
        return Resource.isBuffered(this);
    }

    public int getId() {
        return Resource.getId(this);
    }

    public void facePlayer() {
        switch (Resource.getPlayer().getToward()) {
            case TOWARD_UP:
                this.setToward(TOWARD_DOWN);
                break;
            case TOWARD_DOWN:
                this.setToward(TOWARD_UP);
                break;
            case TOWARD_LEFT:
                this.setToward(TOWARD_RIGHT);
                break;
            case TOWARD_RIGHT:
                this.setToward(TOWARD_LEFT);
                break;
        }
    }

    public void onRefresh() {
        this.action();
        refreshMove();
        if (this.isPlayer()) {
            refreshMove();
        }
    }

    public boolean isLastMovingSucceed() {
        return lastMovingSucceed;
    }

    private void refreshMove() {
        for (int i = (int)speed; i > 0; i--) {
            tryMove();
            this.changeViewFromMoving();
        }
        movingBuffer += speed % 1;
        if (movingBuffer >= 1) {
            movingBuffer--;
            tryMove();
            this.changeViewFromMoving();
        }
    }

    private void tryMove() {
        lastMovingSucceed = this.canEntityMove();
        if (lastMovingSucceed) {
            this.move();
        }
    }

    public abstract void action();

    public abstract void onUse();

    @Override
    public void onRegisiter() {

    }

    @Override
    public void onUnRegisiter() {

    }
}
