package com.ectrpg.model.fight;

import com.ectrpg.model.LocationPair;
import com.ectrpg.model.entity.HeartEntity;
import com.ectrpg.controller.LambdaFactory;
import org.frice.obj.SideEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class SlaveFormula extends Formula {
    private final TopFormula top;
    public SlaveFormula(@Nullable HeartEntity owner, int formulaLevel, int resourceType, int resource, int size, @NotNull LocationPair<Float> location, @NotNull TopFormula top) {
        super(owner, formulaLevel, resourceType, resource, size, location);
        this.top = top;
    }

    public Formula makeFormula(int formulaLevel, int resourceType, int resource, int size, @NotNull LocationPair<Float> location, @NotNull SideEffect active, @Nullable SideEffect onRegisiter, @Nullable SideEffect onUnRegisiter) {
        return LambdaFactory.slaveFormulaFactory(this.getOwner(), formulaLevel, resourceType, resource, size, location, active, top);
    }

    @Override
    public void onRegisiter() {
        top.addSlave(this);
    }

    @Override
    public void onUnRegisiter() {

    }
}
