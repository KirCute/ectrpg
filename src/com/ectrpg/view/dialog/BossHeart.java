package com.ectrpg.view.dialog;

import com.ectrpg.model.entity.Boss;
import com.ectrpg.controller.service.Resource;
import com.ectrpg.view.ResourcesManager;
import org.frice.obj.AttachedAbstractObjects;
import org.frice.obj.AttachedObjects;
import org.frice.obj.sub.ImageObject;

import java.util.ArrayList;
import java.util.List;

public class BossHeart implements IDialog {
    public static final int POSITION_X = 0;
    public static final int POSITION_Y = 0;
    public static final int UNIT = 0;
    private final Boss boss;
    private final AttachedAbstractObjects top = new AttachedAbstractObjects(new ArrayList<>());
    private boolean changing = false;
    private List<ImageObject> readyLives = new ArrayList<>();
    private List<ImageObject> nowheart = new ArrayList<>();
    private int lives = 0;
    private int heart = 0;

    public BossHeart(Boss boss) {
        this.boss = boss;
        top.addObject(Resource.COLORLESS);
    }

    @Override
    public AttachedAbstractObjects getTopAttachedObjects() {
        return top;
    }

    @Override
    public AttachedObjects getBottomAttachedObjects() {
        return null;
    }

    @Override
    public int getResource() {
        return -1;
    }

    @Override
    public double getX() {
        return POSITION_X;
    }

    @Override
    public double getY() {
        return POSITION_Y;
    }

    @Override
    public void onRefresh() {
        if (changing) {
            if (lives != boss.getLives()) {
                if (lives < boss.getLives()) {
                    ImageObject newLives = new ImageObject(ResourcesManager.get(3).part(6, 0, 6, 6), POSITION_X + lives * UNIT, POSITION_Y);
                    readyLives.add(newLives);
                    top.addObject(newLives);
                    lives++;
                } else if (lives > boss.getLives()) {
                    nowheart.forEach(top::removeObject);
                    lives--;
                }
                //TODO elif lives == boss.getLives() then place heart
            }
        }
    }

    @Override
    public void startCome() {
        changing = true;
    }
}
