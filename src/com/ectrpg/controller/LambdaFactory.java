package com.ectrpg.controller;

import com.ectrpg.model.LocationPair;
import com.ectrpg.model.entity.Entity;
import com.ectrpg.model.entity.FriendlyNPC;
import com.ectrpg.model.entity.HeartEntity;
import com.ectrpg.model.fight.Bullet;
import com.ectrpg.model.fight.SlaveFormula;
import com.ectrpg.model.fight.TopFormula;
import com.ectrpg.model.fight.buff.Buff;
import com.ectrpg.model.map.item.Item;
import org.frice.obj.SideEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public final class LambdaFactory {
    private static final SideEffect empty = () -> {
    };

    private LambdaFactory() {
    }

    public static Entity entityFactory(@NotNull LocationPair<Float> location, int toward, @NotNull String name,
                                       @NotNull SideEffect active, @NotNull SideEffect onUse,
                                       @Nullable SideEffect onRegisiter, @Nullable SideEffect onUnRegisiter) {
        if (onRegisiter == null) {
            onRegisiter = empty;
        }
        if (onUnRegisiter == null) {
            onUnRegisiter = empty;
        }

        @NotNull SideEffect finalOnRegisiter = onRegisiter;
        @NotNull SideEffect finalOnUnRegisiter = onUnRegisiter;
        return new Entity(location, toward, name) {
            @Override
            public void action() {
                active.invoke();
            }

            @Override
            public void onUse() {
                onUse.invoke();
            }

            @Override
            public void onRegisiter() {
                finalOnRegisiter.invoke();
            }

            @Override
            public void onUnRegisiter() {
                finalOnUnRegisiter.invoke();
            }
        };
    }

    public static Entity npcFactory(@NotNull LocationPair<Float> location, int toward, @NotNull String name, int liveArea,
                                    @NotNull SideEffect onUse, @Nullable SideEffect onRegisiter, @Nullable SideEffect onUnRegisiter,
                                    int width, int height, int shift_x, int shift_y, @NotNull List<String> s) {
        if (onRegisiter == null) {
            onRegisiter = empty;
        }
        if (onUnRegisiter == null) {
            onUnRegisiter = empty;
        }

        @NotNull SideEffect finalOnRegisiter = onRegisiter;
        @NotNull SideEffect finalOnUnRegisiter = onUnRegisiter;
        return new FriendlyNPC(location, toward, name, liveArea, width, height, shift_x, shift_y, s) {
            @Override
            public void onUse() {
                onUse.invoke();
            }

            @Override
            public void onRegisiter() {
                finalOnRegisiter.invoke();
            }

            @Override
            public void onUnRegisiter() {
                finalOnUnRegisiter.invoke();
            }
        };
    }

    public static Entity heartEntityFactory(@NotNull LocationPair<Float> location, int toward, @NotNull String name, int heart, byte lives,
                                            @NotNull SideEffect active, @NotNull SideEffect onUse, @NotNull Consumer<Bullet> onMiss,
                                            @NotNull Consumer<HeartEntity> onDeath, @Nullable SideEffect onRegisiter,
                                            @Nullable SideEffect onUnRegisiter, @Nullable Consumer<Bullet> onGraze, @NotNull List<Buff> buffs,
                                            @NotNull SideEffect failed) {
        if (onRegisiter == null) {
            onRegisiter = empty;
        }
        if (onUnRegisiter == null) {
            onUnRegisiter = empty;
        }
        if (onGraze == null) {
            onGraze = bullet -> {
            };
        }

        @NotNull Consumer<Bullet> finalOnGraze = onGraze;
        @NotNull SideEffect finalOnRegisiter = onRegisiter;
        @NotNull SideEffect finalOnUnRegisiter = onUnRegisiter;
        return new HeartEntity(location, toward, name, heart, lives, buffs) {
            @Override
            public void action() {
                active.invoke();
            }

            @Override
            public void onUse() {
                onUse.invoke();
            }

            @Override
            public void onMiss(Bullet bullet) {
                onMiss.accept(bullet);
            }

            @Override
            public void onDeath(HeartEntity killer) {
                onDeath.accept(killer);
            }

            @Override
            public void onRegisiter() {
                finalOnRegisiter.invoke();
            }

            @Override
            public void onUnRegisiter() {
                finalOnUnRegisiter.invoke();
            }

            @Override
            public void onGraze(Bullet bullet) {
                finalOnGraze.accept(bullet);
            }

            @Override
            public void onFailed() {
                failed.invoke();
            }
        };
    }

    public static Item itemFactory(@NotNull String name, @NotNull LocationPair<Integer> location, @NotNull SideEffect onUse,
                                   @Nullable SideEffect onRegisiter, @Nullable SideEffect onUnRegisiter) {
        if (onRegisiter == null) {
            onRegisiter = empty;
        }
        if (onUnRegisiter == null) {
            onUnRegisiter = empty;
        }

        @NotNull SideEffect finalOnRegisiter = onRegisiter;
        @NotNull SideEffect finalOnUnRegisiter = onUnRegisiter;
        return new Item(name, location) {
            @Override
            public void onUse() {
                onUse.invoke();
            }

            @Override
            public void onRegisiter() {
                finalOnRegisiter.invoke();
            }

            @Override
            public void onUnRegisiter() {
                finalOnUnRegisiter.invoke();
            }
        };
    }

    public static TopFormula topFormulaFactory(@Nullable HeartEntity owner, int formulaLevel, int resourceType, int resource,
                                               int size, LocationPair<Float> location, @NotNull SideEffect active,
                                               @Nullable String name) {
        return new TopFormula(owner, formulaLevel, resourceType, resource, size, location, name) {
            @Override
            public void active() {
                active.invoke();
            }
        };
    }

    public static SlaveFormula slaveFormulaFactory(@Nullable HeartEntity owner, int formulaLevel, int resourceType, int resource,
                                                   int size, LocationPair<Float> location, @NotNull SideEffect active,
                                                   @NotNull TopFormula top) {
        return new SlaveFormula(owner, formulaLevel, resourceType, resource, size, location, top) {
            @Override
            public void active() {
                active.invoke();
            }
        };
    }

    public static Bullet bulletFactory(@Nullable HeartEntity owner, int resourceType, int resource, int damage, 
                                       @NotNull LocationPair<Float> location, @Nullable SideEffect active, 
                                       @Nullable SideEffect onRegisiter, @Nullable SideEffect onUnRegisiter) {
        if ((active == null) && (onRegisiter == null) && (onUnRegisiter == null)) {
            return new Bullet(owner, resourceType, resource, damage, location);
        }
        if (active == null) {
            active = empty;
        }
        if (onRegisiter == null) {
            onRegisiter = empty;
        }
        if (onUnRegisiter == null) {
            onUnRegisiter = empty;
        }

        @NotNull SideEffect finalOnRegisiter = onRegisiter;
        @NotNull SideEffect finalOnUnRegisiter = onUnRegisiter;
        @NotNull SideEffect finalActive = active;
        return new Bullet(owner, resourceType, resource, damage, location) {
            @Override
            public void active() {
                finalActive.invoke();
            }

            @Override
            public void onRegisiter() {
                finalOnRegisiter.invoke();
            }

            @Override
            public void onUnRegisiter() {
                finalOnUnRegisiter.invoke();
            }
        };
    }
}
