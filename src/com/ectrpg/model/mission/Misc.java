package com.ectrpg.model.mission;

import com.ectrpg.controller.service.Resource;
import org.frice.obj.SideEffect;

import java.io.Serializable;

public abstract class Misc implements SideEffect, Serializable {
    protected void completed() {
        Resource.unRegisiterMisc(this);
    }
}
