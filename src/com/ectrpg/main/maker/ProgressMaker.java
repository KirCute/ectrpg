package com.ectrpg.main.maker;

import com.ectrpg.controller.Keyboard;
import com.ectrpg.controller.service.Resource;
import com.ectrpg.db.Localization;
import com.ectrpg.event.EventManager;
import com.ectrpg.model.LocationPair;
import com.ectrpg.model.entity.Entity;
import com.ectrpg.model.entity.FriendlyNPC;
import com.ectrpg.model.fight.buff.SpeedBuff;
import com.ectrpg.model.map.item.Item;
import com.ectrpg.model.mission.Progress;
import com.ectrpg.view.GameFrame;
import com.ectrpg.view.dialog.SimpleTalkingDialog;
import com.ectrpg.view.dialog.TalkingDialog;
import com.ectrpg.view.dialog.obj.Case;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

public final class ProgressMaker {
    private ProgressMaker() {
    }

    public static void main(String[] args) {
        // walk a block use 32 gameticks


        /*
        int progressId = -1;
        Map<Integer, Set<Item>> progressItems = new HashMap<>();
        Set<Item> map000item = new HashSet<>();
        map000item.add(new Door("TestDoor", new LocationPair<>(9,0), new Pair<>(1, new LocationPair<>(2F, 2.9375F)), Entity.TOWARD_UP));
        Set<Item> map001item = new HashSet<>();
        map001item.add(new Door("TestDoor", new LocationPair<>(2,4), new Pair<>(0, new LocationPair<>(9F, 1.0625F)), Entity.TOWARD_DOWN));
        map001item.add(new Door("TestDoor", new LocationPair<>(2,0), new Pair<>(2, new LocationPair<>(9F, 12.9375F)), Entity.TOWARD_UP));
        Set<Item> map002item = new HashSet<>();
        map002item.add(new Door("TestDoor", new LocationPair<>(9,14), new Pair<>(1, new LocationPair<>(2F, 1.0625F)), Entity.TOWARD_DOWN));
        progressItems.put(0, map000item);
        progressItems.put(1, map001item);
        progressItems.put(2, map002item);
        Map<Integer, Set<Entity>> progressEntities = new HashMap<>();
        boolean withDefaultProgress = true;
        */


        ///*
        int progressId = 1;
        Map<Integer, Set<Entity>> progressEntities = new HashMap<>();
        Set<Entity> map000entity = new HashSet<>();
        map000entity.add(new Entity(new LocationPair<>(9.5F, 12.5F), Entity.TOWARD_RIGHT, "Cirno") {
            int useRefresh = 10;
            int activeTime = 80;
            boolean acting = true;

            @Override
            public void onRegisiter() {
                this.setMovingWay(MOVING_FASTER);
            }

            @Override
            public void onUnRegisiter() {
                System.out.println("Entity was unregisitered.");
            }

            @Override
            public void action() {
                if (acting) {
                    if (activeTime >= 0) {
                        this.setToward(Entity.TOWARD_RIGHT);
                        this.setWonderMoving(Entity.GOTOWARD_RIGHT);
                    } else {
                        this.setToward(Entity.TOWARD_LEFT);
                        this.setWonderMoving(Entity.GOTOWARD_LEFT);
                    }
                }
                if (isLastMovingSucceed()) {
                    if (++activeTime >= 160) {
                        activeTime = -160;
                    }
                }
                if (useRefresh > 0) {
                    useRefresh--;
                }
            }

            @Override
            public void onUse() {
                if (useRefresh == 0) {
                    Keyboard.setState(Keyboard.STATE_DIALOG);
                    acting = false;
                    int moving = this.getMoving();
                    int wonderMoving = this.getWonderMoving();
                    int toward = this.getToward();
                    this.setMoving(Entity.NOTGO);
                    this.setWonderMoving(Entity.NOTGO);
                    this.facePlayer();
                    System.out.println("Changed the state of Keyboard.");
                    Case progress = new Case(Localization.query("test.entity.prs"), 2, () -> {
                        Keyboard.setState(Keyboard.STATE_MOVING);
                        acting = true;
                        this.setMoving(moving);
                        this.setWonderMoving(wonderMoving);
                        this.setToward(toward);
                        EventManager.getInstance().performedNp(this, 0);
                        System.out.println("Progress ID is now 0.");
                        System.out.println("Changed the state of Keyboard.");
                        useRefresh = 40;
                    });
                    Case hello = new Case(Localization.query("test.entity.faq"), 3, () -> {
                        ArrayList<String> str = new ArrayList<>();
                        str.add(Localization.query("test.entity.hello1"));
                        str.add(Localization.query("test.entity.hello2"));
                        GameFrame.getInstance().regisiterDialog(new SimpleTalkingDialog(str, () -> {
                            Keyboard.setState(Keyboard.STATE_MOVING);
                            acting = true;
                            this.setMoving(moving);
                            this.setWonderMoving(wonderMoving);
                            this.setToward(toward);
                            useRefresh = 40;
                        }));
                        System.out.println("Changed the state of Keyboard.");
                    });
                    Case back = new Case(Localization.query("test.entity.buff"), 4, () -> {
                        Keyboard.setState(Keyboard.STATE_MOVING);
                        acting = true;
                        this.setMoving(moving);
                        this.setWonderMoving(wonderMoving);
                        this.setToward(toward);
                        Resource.getPlayer().addBuff(new SpeedBuff(600, 1.5F));
                        System.out.println("Changed the state of Keyboard.");
                        useRefresh = 40;
                    });
                    progress.init(hello, null, hello);
                    hello.init(back, progress, back);
                    back.init(null, hello, null);
                    List<Case> cases = new ArrayList<>();
                    cases.add(progress);
                    cases.add(hello);
                    cases.add(back);
                    ArrayList<String> strs = new ArrayList<>();
                    strs.add(Localization.query("test.entity.title"));
                    GameFrame.getInstance().regisiterDialog(new TalkingDialog(strs, cases));
                }
                useRefresh = 10;
            }
        });
        Set<Entity> map002entity = new HashSet<>();
        List<String> greet = new ArrayList<>();
        greet.add("Hello");
        map002entity.add(new FriendlyNPC(new LocationPair<>(9.5F, 7.5F), Entity.TOWARD_DOWN, "Sanae", 3, 30, 12, 2 , -12, greet));
        progressEntities.put(0, map000entity);
        progressEntities.put(2, map002entity);
        Map<Integer, Set<Item>> progressItems = new HashMap<>();
        boolean withDefaultProgress = true;
        //*/


        /*
        int progressId = 100;
        Map<Integer, Set<Entity>> progressEntities = new HashMap<>();
        Map<Integer, Set<Item>> progressItems = new HashMap<>();
        boolean withDefaultProgress = true;
        */


        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("resources/object/progress", "progress" + String.format("%03d", progressId) + ".prs")));
            oos.writeObject(new Progress(progressId, progressEntities, progressItems, withDefaultProgress));
            oos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
