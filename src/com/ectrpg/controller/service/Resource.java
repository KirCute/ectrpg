package com.ectrpg.controller.service;

import com.ectrpg.controller.Keyboard;
import com.ectrpg.event.EventManager;
import com.ectrpg.event.PlayerInDoorEvent;
import com.ectrpg.model.LocationPair;
import com.ectrpg.model.Regisiterable;
import com.ectrpg.model.entity.Boss;
import com.ectrpg.model.entity.Entity;
import com.ectrpg.model.entity.Player;
import com.ectrpg.model.map.Maps;
import com.ectrpg.model.map.item.Item;
import com.ectrpg.model.mission.Progress;
import com.ectrpg.model.save.Save;
import com.ectrpg.view.GameFrame;
import org.frice.obj.FObject;
import org.frice.obj.SideEffect;
import org.frice.obj.sub.ImageObject;
import org.frice.obj.sub.ShapeObject;
import org.frice.resource.graphics.ColorResource;
import org.frice.resource.image.FileImageResource;
import org.frice.resource.image.ImageResource;
import org.frice.util.shape.FRectangle;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class Resource {
    public static final FObject COLORLESS = new ShapeObject(ColorResource.COLORLESS, new FRectangle(1, 1));

    private static Save save = new Save();
    private static final List<Entity> entities = new ArrayList<>();
    private static final List<ImageObject> entitiesImage = new ArrayList<>();
    private static final List<ArrayList<ImageResource>> entitiesResources = new ArrayList<>();
    private static final List<Integer> entitiesProgress = new ArrayList<>();
    private static final List<Entity> entitiesBuffer = new ArrayList<>();
    private static final List<ImageObject> entitiesImageBuffer = new ArrayList<>();
    private static final List<ArrayList<ImageResource>> entitiesResourcesBuffer = new ArrayList<>();
    private static final List<Integer> entitiesProgressBuffer = new ArrayList<>();
    private static final List<Item> items = new ArrayList<>();
    private static final List<ImageObject> itemsImage = new ArrayList<>();
    private static final List<ArrayList<ImageResource>> itemsResources = new ArrayList<>();
    private static final List<Integer> itemsProgress = new ArrayList<>();
    private static final List<SideEffect> misc = new ArrayList<>();
    private static FObject mapBlockImage;
    private static FObject mapBackground;
    private static FObject uponImage;
    private static FObject bossHeart = null;
    private static Maps map;
    private static Boss fightingBoss = null;

    static {
        EventManager.getInstance().addNpListener(event -> {
            if (event.getNextProgress() < -1) {
                save.setTimeId(event.getNextProgress());
                Progress progress = loadMissionProgress();
                Progress nextProgress = loadTimeProgress(progress.withDefaultProgress());
                registerItems(nextProgress);
                regisiterReadyEntities(nextProgress);
            } else if (event.getNextProgress() > -1){
                save.setProgressId(event.getNextProgress());
                Progress nextProgress = loadMissionProgress();
                registerItems(nextProgress);
                regisiterReadyEntities(nextProgress);
            }
        });
    }

    private Resource() {
    }

    public static void refresh() {
        entitiesMove();
        playerUse();
        progressReplacer();
        invokeMisc();
        GameFrame.getInstance().refreshDialog();
        save.refreshTime();
    }

    public static void startBossFighting(Boss boss) {
        if (!entities.contains(boss)) {
            registerEntity(boss, save.getProgressId());
        }

    }

    public static void regisiterMisc(SideEffect m) {
        misc.add(m);
    }

    public static void unRegisiterMisc(SideEffect m) {
        misc.remove(m);
    }

    private static void invokeMisc() {
        for (SideEffect m : misc) {
            m.invoke();
        }
    }

    private static void progressReplacer() {
        for (int i = 0; i < items.size(); i++) {
            if ((itemsProgress.get(i) >= 0 && itemsProgress.get(i) != save.getProgressId()) ||
                    (itemsProgress.get(i) < -1 && itemsProgress.get(i) != save.getTimeId())) {
                unRegisterItem(i);
                i--;
            }
        }
        for (int i = 0; i < entities.size(); i++) {
            if ((entitiesProgress.get(i) >= 0 && entitiesProgress.get(i) != save.getProgressId()) ||
                    (entitiesProgress.get(i) < -1 && entitiesProgress.get(i) != save.getTimeId())) {
                LocationPair<Float> location = entities.get(i).getLocation();
                LocationPair<Float> playerLocation = getPlayer().getLocation();
                if (location.getX() < playerLocation.getX() - 10 ||
                        location.getX() > playerLocation.getX() + 10 ||
                        location.getY() < playerLocation.getY() - 8 ||
                        location.getY() > playerLocation.getY() + 8) {
                    unRegisterEntity(i);
                    i--;
                }
            }
        }
        for (int i = 0; i < entitiesBuffer.size(); i++) {
            if ((entitiesProgressBuffer.get(i) >= 0 && entitiesProgressBuffer.get(i) != save.getProgressId()) ||
                    (entitiesProgressBuffer.get(i) < -1 && entitiesProgressBuffer.get(i) != save.getTimeId())) {
                entitiesBuffer.remove(i);
                entitiesResourcesBuffer.remove(i);
                entitiesImageBuffer.remove(i);
                entitiesProgressBuffer.remove(i);
                i--;
                continue;
            }
            LocationPair<Float> location = entitiesBuffer.get(i).getLocation();
            LocationPair<Float> playerLocation = getPlayer().getLocation();
            if (location.getX() < playerLocation.getX() - 10 ||
                    location.getX() > playerLocation.getX() + 10 ||
                    location.getY() < playerLocation.getY() - 8 ||
                    location.getY() > playerLocation.getY() + 8) {
                registerEntityFromBuffer(i);
                i--;
            }
        }
    }

    public static FObject getMapImage() {
        return mapBlockImage;
    }

    public static void playerInDoor(PlayerInDoorEvent event) {
        Resource.clearEntities();
        LocationPair<Float> pair = event.getTeleport().getY();
        Resource.getPlayer().setLocation(new LocationPair<>(pair.getX() + 0.5F, pair.getY() + 0.5F));
        Resource.getPlayer().setToward(event.getToward());
        save.setMapId(event.getTeleport().getX());
        Resource.registerEntity(Resource.getPlayer(), -1);
        Resource.clearItems();
        Resource.loadMap();
        Progress alwaysProgress = loadAlwaysProgress();
        Resource.registerEntities(alwaysProgress);
        Resource.registerItems(alwaysProgress);
        Progress missionProgress = loadMissionProgress();
        Resource.registerEntities(missionProgress);
        Resource.registerItems(missionProgress);
        Progress defaultProgress = loadDefaultProgress(missionProgress.withDefaultProgress());
        Resource.registerEntities(defaultProgress);
        Resource.registerItems(defaultProgress);
        Progress timeProgress = loadTimeProgress(missionProgress.withDefaultProgress());
        Resource.registerEntities(timeProgress);
        Resource.registerItems(timeProgress);
    }

    public static void unRegisterEntity(int i) {
        entities.get(i).onUnRegisiter();
        entities.remove(i);
        GameFrame.getInstance().removeObject(GameFrame.LAYER_ENTITY, entitiesImage.get(i));
        entitiesImage.remove(i);
        entitiesResources.remove(i);
        entitiesProgress.remove(i);
    }

    public static void unRegisterItem(int i) {
        items.get(i).onUnRegisiter();
        items.remove(i);
        GameFrame.getInstance().removeObject(GameFrame.LAYER_ITEM, itemsImage.get(i));
        itemsImage.remove(i);
        itemsResources.remove(i);
        itemsProgress.remove(i);
    }

    public static FObject getBackground() {
        return mapBackground;
    }

    public static FObject getUponImage() {
        return uponImage;
    }

    public static Player getPlayer() {
        return (entities.size() == 0 || entities.get(0) == null) ? save.getPlayer() : (Player) entities.get(0);
    }

    public static List<Entity> getRegisteredEntities() {
        return entities;
    }

    public static List<ImageObject> getEntitiesImage() {
        return entitiesImage;
    }

    public static List<ArrayList<ImageResource>> getEntitiesResources() {
        return entitiesResources;
    }

    public static List<Item> getRegisteredItems() {
        return items;
    }

    public static List<ImageObject> getItemsImage() {
        return itemsImage;
    }

    public static List<ArrayList<ImageResource>> getItemsResources() {
        return itemsResources;
    }

    public static Item getItem(LocationPair<Integer> location) {
        for (Item i : items) {
            if (i.getLocation().equals(location)) {
                return i;
            }
        }
        return null;
    }

    public static Entity getEntity(LocationPair<Integer> location) {
        for (Entity e : entities) {
            if (e.toIntegerPair().equals(location)) {
                return e;
            }
        }
        return null;
    }

    public static int getId(Entity entity) {
        return entities.indexOf(entity);
    }

    public static int getId(Item item) {
        return items.indexOf(item);
    }

    public static boolean isRegisitered(Item item) {
        return items.contains(item);
    }

    public static boolean isRegisitered(Entity entity) {
        return entities.contains(entity);
    }

    public static boolean isBuffered(Entity entity) {
        return entitiesBuffer.contains(entity);
    }

    public static void unRegisterEntity(Entity entity) {
        if (entities.contains(entity)) {
            unRegisterEntity(entities.indexOf(entity));
        }
    }

    public static void unRegisterItem(Item item) {
        if (items.contains(item)) {
            unRegisterEntity(items.indexOf(item));
        }
    }

    public static int getMapId() {
        return save.getMapId();
    }

    public static String getTime() {
        return save.getTime();
    }

    private static void playerUse() {
        if (Keyboard.isKeyPressed(Keyboard.KEY_USE, Keyboard.STATE_MOVING)) {
            Player p = getPlayer();
            LocationPair<Integer> location = p.toIntegerPair();
            LocationPair<Integer> side = p.getFront();
            Item item = Resource.getItem(location);
            if (item != null) {
                item.onUse();
            }
            item = Resource.getItem(side);
            if (item != null) {
                item.onUse();
            }
            Entity entity = Resource.getEntity(side);
            if (entity != null) {
                entity.onUse();
            }
        }
    }

    private static void entitiesMove() {
        for (Entity entity : entities) {
            entity.onRefresh();
        }
    }

    public static Maps getMap() {
        return map;
    }

    private static void clearEntities() {
        for (Regisiterable entity : entities) {
            entity.onUnRegisiter();
        }
        entities.clear();
        for (FObject image : entitiesImage) {
            GameFrame.getInstance().removeObject(GameFrame.LAYER_ENTITY, image);
        }
        entitiesImage.clear();
        entitiesResources.clear();
        entitiesProgress.clear();
        entitiesBuffer.clear();
        entitiesImageBuffer.clear();
        entitiesResourcesBuffer.clear();
        entitiesProgressBuffer.clear();
    }

    private static void clearItems() {
        for (Regisiterable item : items) {
            item.onUnRegisiter();
        }
        items.clear();
        for (FObject image : itemsImage) {
            GameFrame.getInstance().removeObject(GameFrame.LAYER_ITEM, image);
        }
        itemsImage.clear();
        itemsResources.clear();
        itemsProgress.clear();
    }

    private static void registerEntityFromBuffer(int entityId) {
        Entity entity = entitiesBuffer.get(entityId);
        entity.onRegisiter();
        entities.add(entity);
        entitiesResources.add(entitiesResourcesBuffer.get(entityId));
        entitiesImage.add(entitiesImageBuffer.get(entityId));
        entitiesProgress.add(entitiesProgressBuffer.get(entityId));
        GameFrame.getInstance().addObject(GameFrame.LAYER_ENTITY, entitiesImage.get(entitiesImage.size() - 1));
        entitiesBuffer.remove(entityId);
        entitiesResourcesBuffer.remove(entityId);
        entitiesImageBuffer.remove(entityId);
        entitiesProgressBuffer.remove(entityId);
    }

    private static void registerEntity(Entity entity, int progressId) {
        entity.onRegisiter();
        entities.add(entity);
        ArrayList<ImageResource> res = new ArrayList<>();
        FileImageResource fir;
        if (entity instanceof Player) {
            fir = new FileImageResource("resources/assets/image/entity/Player.png");
        } else {
            fir = new FileImageResource("resources/assets/image/entity/" + entity.getName() + ".png");
        }
        for (int adding = 0; adding < 10; adding++) {
            ImageResource pir;
            try {
                pir = fir.part(adding * 32, 0, 32, 32);
            } catch (Exception e) {
                pir = null;
            }
            res.add(pir);
        }
        entitiesResources.add(res);
        entitiesImage.add(new ImageObject(entitiesResources.get(entitiesResources.size() - 1).get(0)));
        entitiesProgress.add(progressId);
        GameFrame.getInstance().addObject(GameFrame.LAYER_ENTITY, entitiesImage.get(entitiesImage.size() - 1));
    }

    private static void registerReadyEntity(Entity entity, int progressId) {
        entitiesBuffer.add(entity);
        ArrayList<ImageResource> res = new ArrayList<>();
        FileImageResource fir = new FileImageResource("resources/assets/image/entity/" + entity.getName() + ".png");
        for (int adding = 0; adding < 10; adding++) {
            ImageResource pir;
            try {
                pir = fir.part(adding * 32, 0, 32, 32);
            } catch (Exception e) {
                pir = null;
            }
            res.add(pir);
        }
        entitiesResourcesBuffer.add(res);
        entitiesImageBuffer.add(new ImageObject(entitiesResourcesBuffer.get(entitiesResourcesBuffer.size() - 1).get(0)));
        entitiesProgressBuffer.add(progressId);
    }

    private static void loadMap() {
        final String mapid = mapid2string();
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("resources/object/map/map" + mapid + ".map"));
            map = (Maps) ois.readObject();
            if (getMapId() != map.getMapId()) {
                throw new RuntimeException("Cannot load correct map.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        try {
            mapBlockImage = new ImageObject(new FileImageResource("resources/assets/image/map/map" + mapid + ".png"),
                    Player.DEFAULT_PLAYER_LOCATION.getX() - Resource.getPlayer().getLocation().getX(),
                    Player.DEFAULT_PLAYER_LOCATION.getY() - Resource.getPlayer().getLocation().getY());
        } catch (Exception e) {
            mapBlockImage = COLORLESS;
        }
        try {
            mapBackground = new ImageObject(new FileImageResource("resources/assets/image/background/map" + mapid + ".png"), 0, 0);
        } catch (Exception e) {
            mapBackground = new ShapeObject(ColorResource.BLACK, new FRectangle(608, 480), 0, 0);
        }
        try {
            uponImage = new ImageObject(new FileImageResource("resources/assets/image/upon/map" + mapid + ".png"),
                    Resource.getPlayer().getLocation().getX(), Resource.getPlayer().getLocation().getY());
        } catch (Exception e) {
            uponImage = COLORLESS;
        }
        GameFrame.getInstance().addObject(GameFrame.LAYER_BACKGROUND, mapBackground);
        GameFrame.getInstance().addObject(GameFrame.LAYER_MAP, mapBlockImage);
        GameFrame.getInstance().addObject(GameFrame.LAYER_UPON, uponImage);
    }

    private static void registerItems(Progress progress) {
        if (progress.getItems(Resource.getMapId()) == null) {
            return;
        }
        for (Item i : progress.getItems(Resource.getMapId())) {
            i.onRegisiter();
            items.add(i);
            try {
                ImageResource fir = new FileImageResource("resources/assets/image/item/" + i.getName() + ".png");
                ArrayList<ImageResource> pir = new ArrayList<>();
                for (int w = 0; w < fir.getImage().getWidth(); w += 32) {
                    pir.add(fir.part(w, 0, 32, 32));
                }
                itemsResources.add(pir);
            } catch (Exception e) {
                // TODO: 2018/7/13 0013 Display crash reporter
            }
            itemsImage.add(new ImageObject(itemsResources.get(itemsResources.size() - 1).get(0)));
            itemsProgress.add(progress.getProgressId());
            GameFrame.getInstance().addObject(GameFrame.LAYER_ITEM, itemsImage.get(itemsImage.size() - 1));
        }
    }

    private static void regisiterReadyEntities(Progress progress) {
        if (progress.getEntities(Resource.getMapId()) == null) {
            return;
        }
        for (Entity e : progress.getEntities(Resource.getMapId())) {
            registerReadyEntity(e, progress.getProgressId());
        }
    }

    private static void registerEntities(Progress progress) {
        if (progress.getEntities(Resource.getMapId()) == null) {
            return;
        }
        for (Entity e : progress.getEntities(Resource.getMapId())) {
            registerEntity(e, progress.getProgressId());
        }
    }

    private static Progress loadMissionProgress() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("resources/object/progress/progress" + progressid2string() + ".prs"));
            return (Progress) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
            return loadDefaultProgress(false);
        }
    }

    private static Progress loadTimeProgress(boolean really) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(really ? "resources/object/progress/progress" + timeid2string() + ".prs" : "resources/object/progress/progress000.prs"));
            return (Progress) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
            return loadDefaultProgress(false);
        }
    }

    private static Progress loadAlwaysProgress() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("resources/object/progress/always.prs"));
            return (Progress) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
            return loadDefaultProgress(false);
        }
    }

    private static Progress loadDefaultProgress(boolean really) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(really ? "resources/object/progress/default.prs" : "resources/object/progress/progress000.prs"));
            return (Progress) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
            return new Progress(0, new HashMap<>(), new HashMap<>(), true);
        }
    }

    private static String mapid2string() {
        return String.format("%03d", save.getMapId());
    }

    private static String progressid2string() {
        return String.format("%03d", save.getProgressId());
    }

    private static String timeid2string() {
        return "T" + String.format("%02d", -save.getTimeId());
    }
}
