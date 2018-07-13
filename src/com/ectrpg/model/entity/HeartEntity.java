package com.ectrpg.model.entity;

import com.ectrpg.model.LocationPair;
import com.ectrpg.model.fight.Bullet;
import com.ectrpg.model.fight.TopFormula;
import com.ectrpg.model.fight.buff.Buff;
import com.ectrpg.controller.LambdaFactory;
import com.ectrpg.model.fight.Formula;
import org.frice.obj.SideEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class HeartEntity extends Entity {
    private int heart;
    private byte lives;
    private final List<Buff> buffs;
    private final List<TopFormula> ministers = new ArrayList<>();

    public HeartEntity(LocationPair<Float> location, int toward, String name, int heart, byte lives, @NotNull List<Buff> buffs) {
        super(location, toward, name);
        this.heart = heart;
        this.lives = lives;
        this.buffs = buffs;
    }

    public List<Buff> getBuffs() {
        return buffs;
    }

    public void addBuff(Buff buff) {
        buff.onRegisiter(this);
        buffs.add(buff);
    }

    public void removeBuff(Buff buff) {
        buffs.remove(buff);
    }

    public void addMinister(TopFormula minister) {
        ministers.add(minister);
    }

    public void removeMinister(TopFormula minister) {
        ministers.remove(minister);
    }

    public void addHeart(int add) {
        this.heart += add;
    }

    public void removeHeart(int add) {
        this.heart -= add;
    }

    public void oneUp() {
        this.lives++;
    }

    public void die() {
        this.lives--;
    }

    public int getHeart() {
        return heart;
    }

    public byte getLives() {
        return lives;
    }

    public Formula makeFormula(int formulaLevel, int resourceType, int resource, int size, @NotNull LocationPair<Float> location,
                               @NotNull SideEffect active, @Nullable String name) {
        return LambdaFactory.topFormulaFactory(this, formulaLevel, resourceType, resource, size, location, active, name);
    }

    public Bullet makeBullet(int resourceType, int resource, int damage, @NotNull LocationPair<Float> location,
                             @NotNull SideEffect active, @Nullable SideEffect onRegisiter, @Nullable SideEffect onUnRegisiter) {
        return LambdaFactory.bulletFactory(this, resourceType, resource, damage, location, active, onRegisiter, onUnRegisiter);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        for (Buff buff : buffs) {
            buff.onRefresh(this);
        }
    }

    public abstract void onMiss(Bullet bullet);

    public abstract void onDeath(HeartEntity killer);

    public abstract void onGraze(Bullet bullet);

    public abstract void onFailed();
}
