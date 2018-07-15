package com.ectrpg.model.entity;

import com.ectrpg.model.LocationPair;
import com.ectrpg.model.Pair;
import com.ectrpg.model.fight.Bullet;
import com.ectrpg.model.fight.buff.Buff;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;

public abstract class Boss extends HeartEntity {
    private final Collection<Consumer<Bullet>> missListeners = new HashSet<>();
    private final Collection<Consumer<Bullet>> grazeListeners = new HashSet<>();
    private final Collection<Consumer<HeartEntity>> killListeners = new HashSet<>();
    private final List<Pair<Integer, Consumer<Boss>>> action;
    private Consumer<Boss> now;

    public Boss(LocationPair<Float> location, int toward, String name, @NotNull List<Buff> buffs, @NotNull List<Pair<Integer, Consumer<Boss>>> action) {
        super(location, toward, name, action.get(action.size() - 1).getX(), (byte) action.size(), buffs);
        this.action = action;
        this.now = this.action.get(action.size() - 1).getY();
    }

    public void addMissListener(Consumer<Bullet> listener) {
        missListeners.add(listener);
    }

    public void removeMissListener(Consumer<Bullet> listener) {
        missListeners.remove(listener);
    }

    public void addGrazeListener(Consumer<Bullet> listener) {
        grazeListeners.add(listener);
    }

    public void removeGrazeListener(Consumer<Bullet> listener) {
        grazeListeners.remove(listener);
    }

    public void addKillListener(Consumer<HeartEntity> listener) {
        killListeners.add(listener);
    }

    public void removeKillListener(Consumer<HeartEntity> listener) {
        killListeners.remove(listener);
    }

    @Override
    public void onMiss(Bullet bullet) {
        for (Consumer<Bullet> listener : missListeners) {
            listener.accept(bullet);
        }
    }

    @Override
    public void onDeath(HeartEntity killer) {
        for (Consumer<HeartEntity> listener : killListeners) {
            listener.accept(killer);
        }
        action.remove(action.size() - 1);
        if (action.size() > 0) {
            this.addHeart(action.get(action.size() - 1).getX());
            this.now = action.get(action.size() - 1).getY();
        }
    }

    public abstract void onFailed();

    @Override
    public void onGraze(Bullet bullet) {
        for (Consumer<Bullet> listener : grazeListeners) {
            listener.accept(bullet);
        }
    }

    @Override
    public void action() {
        now.accept(this);
    }

    @Override
    public void onUse() {
    }
}
