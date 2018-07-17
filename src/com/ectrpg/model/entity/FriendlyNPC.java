package com.ectrpg.model.entity;

import com.ectrpg.controller.service.Resource;
import com.ectrpg.model.LocationPair;
import com.ectrpg.view.GameFrame;
import com.ectrpg.view.dialog.NpcGreets;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FriendlyNPC extends Entity {
    private LocationPair<Integer> spawn;
    private int activeZone;
    private float keepTime = 0f;
    private NpcGreets greet;
    private int useRefresh = 0;

    public FriendlyNPC(LocationPair<Float> location, int toward, String name, int activeZone, @NotNull List<String> s) {
        super(location, toward, name);
        this.spawn = location.toIntegerPair();
        this.activeZone = activeZone;
        this.greet = new NpcGreets(this, s);
    }

    @Override
    protected boolean canEntityMoveTo(int side) {
        return Math.abs(this.getSideBlock(side, SIDE_NEXTMIDDLE).getX() - spawn.getX()) <= activeZone &&
                Math.abs(this.getSideBlock(side, SIDE_NEXTMIDDLE).getY() - spawn.getY()) <= activeZone &&
                super.canEntityMoveTo(side);
    }

    @Override
    public boolean canEntityMove() {
        switch (this.getWonderMoving()) {
            case GOTOWARD_UP:
                this.setMoving(this.getWonderMoving());
                return this.canEntityMoveTo(SIDE_UP);
            case GOTOWARD_DOWN:
                this.setMoving(this.getWonderMoving());
                return this.canEntityMoveTo(SIDE_DOWN);
            case GOTOWARD_LEFT:
                this.setMoving(this.getWonderMoving());
                return this.canEntityMoveTo(SIDE_LEFT);
            case GOTOWARD_RIGHT:
                this.setMoving(this.getWonderMoving());
                return this.canEntityMoveTo(SIDE_RIGHT);
            case GOTOWARD_UPLEFT:
                if (this.canEntityMoveTo(SIDE_UP) && this.canEntityMoveTo(SIDE_LEFT)) {
                    this.setMoving(this.getWonderMoving());
                    return true;
                }
                return false;
            case GOTOWARD_UPRIGHT:
                if (this.canEntityMoveTo(SIDE_UP) && this.canEntityMoveTo(SIDE_RIGHT)) {
                    this.setMoving(this.getWonderMoving());
                    return true;
                }
                return false;
            case GOTOWARD_DOWNLEFT:
                if (this.canEntityMoveTo(SIDE_DOWN) && this.canEntityMoveTo(SIDE_LEFT)) {
                    this.setMoving(this.getWonderMoving());
                    return true;
                }
                return false;
            case GOTOWARD_DOWNRIGHT:
                if (this.canEntityMoveTo(SIDE_DOWN) && this.canEntityMoveTo(SIDE_RIGHT)) {
                    this.setMoving(this.getWonderMoving());
                    return true;
                }
                return false;
            default:
                this.setMoving(this.getWonderMoving());
                return false;
        }
    }

    @Override
    public void action() {
        if (keepTime == .0f) {
            this.changeTowardFromMoving();
        }
        if (Resource.getRandom().nextInt(100) < 110.0f - keepTime) {
            keepTime += .2f;
        } else {
            if (this.getMoving() != NOTGO) {
                this.setWonderMoving(NOTGO);
            } else {
                this.setWonderMoving(Resource.getRandom().nextInt(8) + 1);
            }
            keepTime = .0f;
        }
        if (!isLastMovingSucceed() || isLastAutoTurn()) {
            this.setMoving(NOTGO);
        }
        greet.onRefresh();
        if (useRefresh > 0) {
            if (--useRefresh == 0) {
                GameFrame.getInstance().unRegisiterDialog(greet);
            }
        }
    }

    @Override
    public void onUse() {
        if (useRefresh > 0) {
            return;
        }
        useRefresh = 600;
        GameFrame.getInstance().regisiterDialog(greet);
    }

    @Override
    public void onRegisiter() {

    }

    @Override
    public void onUnRegisiter() {

    }
}
