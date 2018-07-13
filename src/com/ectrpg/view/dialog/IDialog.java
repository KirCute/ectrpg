package com.ectrpg.view.dialog;

import org.frice.obj.AttachedAbstractObjects;
import org.frice.obj.AttachedObjects;

public interface IDialog {
    AttachedAbstractObjects getTopAttachedObjects();
    AttachedObjects getBottomAttachedObjects();
    int getResource();
    double getX();
    double getY();
    void onRefresh();
    void startCome();
}
