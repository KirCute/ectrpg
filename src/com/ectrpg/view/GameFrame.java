package com.ectrpg.view;

import com.ectrpg.controller.Keyboard;
import com.ectrpg.controller.service.Resource;
import com.ectrpg.db.Localization;
import com.ectrpg.event.EventManager;
import com.ectrpg.event.PlayerInDoorEvent;
import com.ectrpg.model.LocationPair;
import com.ectrpg.model.Pair;
import com.ectrpg.model.entity.Player;
import com.ectrpg.view.dialog.IDialog;
import org.frice.Game;
import org.frice.Initializer;
import org.frice.event.Events;
import org.frice.obj.AbstractObject;
import org.frice.obj.FObject;
import org.frice.obj.button.SimpleButton;
import org.frice.obj.sub.ImageObject;
import org.frice.util.message.FLog;
import org.frice.util.time.FTimer;

import java.awt.*;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public final class GameFrame extends Game {
    public static final int LAYER_BACKGROUND = 0;
    public static final int LAYER_MAP = 1;
    public static final int LAYER_ITEM = 2;
    public static final int LAYER_ENTITY = 3;
    public static final int LAYER_UPON = 4;
    public static final int LAYER_DIALOGTABLE = 5;
    public static final int LAYER_DIALOGCASE = 6;
    public static final int LAYER_DIALOGTEXT = 7;

    private static final GameFrame INSTANCE = new GameFrame();
    private final Set<Pair<IDialog, FObject>> dialogs = new HashSet<>();
    private final FTimer debug = new FTimer(500);
    private final Comparator<AbstractObject> entitiesComparator = Comparator.comparingDouble(AbstractObject::getY);
    private long fpsTime;
    private boolean running = false;

    private GameFrame() {
        super(8);
    }

    public static GameFrame getInstance() {
        return INSTANCE;
    }

    @Override
    public void onInit() {
        super.onInit();
        this.setShowFPS(false);
        this.setAutoGC(false);
        this.setBackground(Color.BLACK);
        this.setVisible(true);
        int leftrightSide = this.getInsets().left + this.getInsets().right;
        int topbuttomSide = this.getInsets().top + this.getInsets().bottom;
        this.setSize(608 + leftrightSide, 480 + topbuttomSide);
        this.setLocationRelativeTo(null);
        this.setMillisToRefresh(0);
        this.addKeyListener(
                null,
                e -> Keyboard.setPressed(e.getKeyCode(), true),
                e -> Keyboard.setPressed(e.getKeyCode(), false)
        );
        EventManager.getInstance().addPdListener(event -> {
            this.readyInDoorAnimation();
            this.removeObject(LAYER_BACKGROUND, Resource.getBackground());
            this.removeObject(LAYER_MAP, Resource.getMapImage());
            this.removeObject(LAYER_UPON, Resource.getUponImage());
            this.readyNewMap(event);
            this.inedDoorAnimation();
        });
    }

    @Override
    public void onLastInit() {
        this.setTitle(Localization.query("title.title"));
        super.onLastInit();
        SimpleButton startButton = new SimpleButton(Localization.query("title.startGame"), 243, 360, 122, 50);
        startButton.setFontName(Localization.getFont());
        startButton.setOnMouseListener(onMouseEvent -> {
            if (onMouseEvent.getType() == Events.MOUSE_CLICKED) {
                //int saveid = 0;    // TODO: 2018/7/12 0012 Select a save or create a new save
                this.removeObject(1, startButton);
                // TODO: 2018/7/13 0013 Display loading interface
            /*
            try {
                Resource.loadSave(saveid);
            } catch (Exception e) {
                // TODO: 2018/7/12 0012 Display crash report
                FLog.e("Failed load save " + Integer.toString(saveid) + ": " + e.getMessage());
                System.exit(1);
            }
            */
                // TODO: 2018/7/13 0013 Make sure
                try {
                    this.readyNewMap(new PlayerInDoorEvent(this, new Pair<>(Resource.getMapId(), Resource.getPlayer().getLocation()), Resource.getPlayer().getToward()));
                    running = true;
                    this.inedDoorAnimation();
                } catch (Exception e) {
                    // TODO: 2018/7/12 0012 Display crash report
                    FLog.e("Failed load map: " + e.getMessage());
                    System.exit(1);
                }
                // TODO: 2018/7/12 0012 When game start
            }
        });
        this.addObject(1, startButton);
    }

    @Override
    public void onExit() {
        super.onExit();
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (this.running) {
            long startTime = System.nanoTime();
            Resource.refresh();
            this.getLayers(LAYER_ENTITY).getObjects().sort(entitiesComparator);
            this.viewRefresh();
            if (this.debug.ended()) {
                try {
                    System.out.print(Resource.getTime());
                    System.out.print("      Player Location: " + Float.toString(Resource.getPlayer().getLocation().getX()) + ", " + Float.toString(Resource.getPlayer().getLocation().getY()));
                    System.out.print("      Player Toward: " + Integer.toString(Resource.getPlayer().getToward()));
                    System.out.print("      Player Moving: " + Integer.toString(Resource.getPlayer().getMoving()));
                    System.out.print("      Player UsingView: " + Integer.toString(Resource.getPlayer().getUsingView()));
                    System.out.print("      Map View Location: " + Double.toString(Resource.getMapImage().getX()) + ", " + Double.toString(Resource.getMapImage().getY()));
                    System.out.print("      Map Id: " + Resource.getMapId());
                    System.out.print("      Speed: " + Resource.getPlayer().getSpeed());
                    System.out.print("      Last: " + Resource.getPlayer().isLastMovingSucceed());
                    System.out.println("      Turn: " + Resource.getPlayer().isLastAutoTurn());
                    Keyboard.show();
                } catch (NullPointerException npe) {
                    System.out.println("No data.");
                }
            }
            try {
                if (System.nanoTime() - startTime < fpsTime) {
                    Thread.sleep((fpsTime - (System.nanoTime() - startTime)) / 1000000L);
                }
            } catch (InterruptedException ignored) {
            }
        }
    }

    public void regisiterDialog(IDialog dialog) {
        FObject table;
        try {
            table = new ImageObject(ResourcesManager.get(dialog.getResource()));
        } catch (Exception e) {
            table = Resource.COLORLESS;
        }
        table.setX(dialog.getX());
        table.setY(dialog.getY());
        dialogs.add(new Pair<>(dialog, table));
        this.addObject(LAYER_DIALOGTABLE, table);
        this.addObject(LAYER_DIALOGCASE, dialog.getBottomAttachedObjects());
        this.addObject(LAYER_DIALOGTEXT, dialog.getTopAttachedObjects());
        dialog.startCome();
    }

    public void refreshDialog() {
        for (Pair<IDialog, FObject> pair : dialogs) {
            pair.getX().onRefresh();
        }
    }

    public void unRegisiterDialog(IDialog dialog) {
        for (Pair<IDialog, FObject> pair : dialogs) {
            if (pair.getX().equals(dialog)) {
                this.removeObject(LAYER_DIALOGTEXT, pair.getX().getTopAttachedObjects());
                this.removeObject(LAYER_DIALOGCASE, pair.getX().getBottomAttachedObjects());
                this.removeObject(LAYER_DIALOGTABLE, pair.getY());
                dialogs.remove(pair);
                return;
            }
        }
    }

    private void viewRefresh() {
        final FObject mapImage = Resource.getMapImage();
        final LocationPair<Float> location = Resource.getPlayer().getLocation();
        final LocationPair<Integer> viewShifting = Resource.getMap().getViewShifting();
        mapImage.setX((Player.DEFAULT_PLAYER_LOCATION.getX() + 0.5F - location.getX()) * 32 + viewShifting.getX());
        mapImage.setY((Player.DEFAULT_PLAYER_LOCATION.getY() + 0.5F - location.getY()) * 32 + viewShifting.getY());
        for (int i = 0; i < Resource.getRegisteredEntities().size(); i++) {
            final ImageObject imageObject = Resource.getEntitiesImage().get(i);
            imageObject.setRes(
                    Resource.getEntitiesResources().get(i).get(Resource.getRegisteredEntities().get(i).getUsingView())
            );
            final LocationPair<Float> location1 = Resource.getRegisteredEntities().get(i).getLocation();
            imageObject.setX((location1.getX() - 0.5F) * 32 + mapImage.getX());
            imageObject.setY((location1.getY() - 0.5F) * 32 + mapImage.getY());
        }
        for (int i = 0; i < Resource.getRegisteredItems().size(); i++) {
            final ImageObject imageObject = Resource.getItemsImage().get(i);
            imageObject.setRes(Resource.getItemsResources().get(i).get(Resource.getRegisteredItems().get(i).getUsingView()));
            final LocationPair<Integer> location1 = Resource.getRegisteredItems().get(i).getLocation();
            imageObject.setX(location1.getX() * 32 + mapImage.getX());
            imageObject.setY(location1.getY() * 32 + mapImage.getY());
        }
    }

    private void readyNewMap(PlayerInDoorEvent event) {
        Resource.playerInDoor(event);
    }

    private void readyInDoorAnimation() {
        // TODO: 18-7-28 Display animation 
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void inedDoorAnimation() {
        // TODO: 18-7-28 Display animation 
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void InitGame(int fps, String font, String lang) {
        getInstance().fpsTime = (long) ((1000.0 / (double) fps) * 1000000);
        Localization.initText(font, lang);
        Initializer.launch(INSTANCE);
    }
}
