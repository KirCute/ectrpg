package com.ectrpg.event;

import com.ectrpg.model.map.item.Door;
import com.ectrpg.model.LocationPair;
import com.ectrpg.model.Pair;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Consumer;

public final class EventManager {
    private static final EventManager instance = new EventManager();

    public static EventManager getInstance() {
        return instance;
    }

    private final Collection<Consumer<NextProgressEvent>> npListeners = new HashSet<>();
    private final Collection<Consumer<PlayerInDoorEvent>> pdListeners = new HashSet<>();

    public void addNpListener(Consumer<NextProgressEvent> listener) {
        npListeners.add(listener);
    }

    public void removeNpListener(Consumer<NextProgressEvent> listener) {
        npListeners.remove(listener);
    }

    public void performedNp(Object source, int nextProgress) {
        if (!npListeners.isEmpty()) {
            NextProgressEvent event = new NextProgressEvent(source, nextProgress);
            this.notifyNpListeners(event);
        }
    }

    private void notifyNpListeners(NextProgressEvent event) {
        for (Consumer<NextProgressEvent> listener : npListeners) {
            listener.accept(event);
        }
    }

    public void addPdListener(Consumer<PlayerInDoorEvent> listener) {
        pdListeners.add(listener);
    }

    public void removePdListener(Consumer<PlayerInDoorEvent> listener) {
        pdListeners.remove(listener);
    }

    public void performedPd(Door source, Pair<Integer, LocationPair<Float>> teleport, int toward) {
        if (!pdListeners.isEmpty()) {
            PlayerInDoorEvent event = new PlayerInDoorEvent(source, teleport, toward);
            this.notifyPdListeners(event);
        }
    }

    private void notifyPdListeners(PlayerInDoorEvent event) {
        for (Consumer<PlayerInDoorEvent> listener : pdListeners) {
            listener.accept(event);
        }
    }
}