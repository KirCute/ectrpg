package com.ectrpg.model.mission;

import com.ectrpg.model.entity.Entity;
import com.ectrpg.model.map.item.Item;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public class Progress implements Serializable {
    private static final long serialVersionUID = 1010991160112114115L;
    private final boolean withDefaultProgress;
    private final int progressId;
    private final Map<Integer, Set<Entity>> progressEntities;
    private final Map<Integer, Set<Item>> progressItems;

    public Progress(int progressId, @NotNull Map<Integer, Set<Entity>> progressEntities, @NotNull Map<Integer, Set<Item>> progressItems, boolean withDefaultProgress) {
        this.withDefaultProgress = withDefaultProgress;
        this.progressId = progressId;
        this.progressEntities = progressEntities;
        this.progressItems = progressItems;
    }

    public int getProgressId() {
        return progressId;
    }

    public Set<Entity> getEntities(int mapId) {
        return progressEntities.get(mapId);
    }

    public Set<Item> getItems(int mapId) {
        return progressItems.get(mapId);
    }

    public Map<Integer, Set<Entity>> getEntities() {
        return progressEntities;
    }

    public Map<Integer, Set<Item>> getItems() {
        return progressItems;
    }

    public boolean withDefaultProgress() {
        return withDefaultProgress;
    }
}
