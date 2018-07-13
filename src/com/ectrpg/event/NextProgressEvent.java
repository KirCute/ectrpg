package com.ectrpg.event;

import java.util.EventObject;

public final class NextProgressEvent extends EventObject {
    private final int nextProgress;

    public NextProgressEvent(Object source, int nextProgress) {
        super(source);
        this.nextProgress = nextProgress;
    }

    public int getNextProgress() {
        return nextProgress;
    }
}
