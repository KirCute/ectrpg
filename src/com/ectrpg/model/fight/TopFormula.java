package com.ectrpg.model.fight;

import com.ectrpg.controller.LambdaFactory;
import com.ectrpg.model.LocationPair;
import com.ectrpg.model.entity.HeartEntity;
import org.frice.obj.SideEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public abstract class TopFormula extends Formula {
    public static final String CLASSICAL = "";
    public static final String NONAME = "";

    private final String name;
    private final ArrayList<SlaveFormula> slaves = new ArrayList<>();
    public TopFormula(@Nullable HeartEntity owner, int formulaLevel, int resourceType, int resource, int size, @NotNull LocationPair<Float> location, @Nullable String name) {
        super(owner, formulaLevel, resourceType, resource, size, location);
        if (name == null) {
            this.name = NONAME + "";
        } else {
            this.name = CLASSICAL + name;
        }
    }

    public String getName() {
        return name;
    }

    public void addSlave(SlaveFormula slave) {
        slaves.add(slave);
    }

    public void removeSlave(SlaveFormula slave) {
        slaves.remove(slave);
    }

    @Override
    public void onRegisiter() {
        super.getOwner().addMinister(this);
    }

    @Override
    public void onUnRegisiter() {
        super.getOwner().removeMinister(this);
        for (SlaveFormula slave : slaves) {
            // TODO: 18-2-9 Destroy slaves
        }
    }

    public SlaveFormula makeFormula(int formulaLevel, int resourceType, int resource, int size, @NotNull LocationPair<Float> location,
                               @NotNull SideEffect active) {
        return LambdaFactory.slaveFormulaFactory(this.getOwner(), formulaLevel, resourceType, resource, size, location, active, this);
    }
}
