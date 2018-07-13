package com.ectrpg.model.save;

import com.ectrpg.model.LocationPair;
import com.ectrpg.model.Pair;
import com.ectrpg.model.entity.Entity;
import com.ectrpg.model.entity.Player;

import java.io.Serializable;
import java.util.ArrayList;

public class Save implements Serializable {
    private static final long serialVersionUID = 1010991160115097118L;
    private Pair<Player, Integer> playerData = new Pair<>(new Player(new LocationPair<>(Player.DEFAULT_PLAYER_LOCATION.getX(),
            Player.DEFAULT_PLAYER_LOCATION.getY()), Entity.TOWARD_DOWN, "PlayerName", 200, (byte) 3, new ArrayList<>()), 0);
    private int progressId = 1;
    private int timeId = -2;
    private byte timeTick = 0;
    private byte timeSec = 0;
    private byte timeMin = 0;
    private byte timeHour = 0;

    private Pair<Player, Integer> getPlayerData() {
        return playerData;
    }

    public void setMapId(int mapId) {
        playerData.setY(mapId);
    }

    public void refreshTime() {
        if (timeTick++ >= 120) {
            timeTick = 0;
            ++timeSec;
        }
        if (timeSec >= 60) {
            timeSec = 0;
            ++timeMin;
        }
        if (timeMin >= 60) {
            timeMin = 0;
            ++timeHour;
        }
        if (timeHour >= 30) {
            timeHour = 0;
        }
    }

    public String getTime() {
        return time2string(timeHour) +
                ":" +
                time2string(timeMin) +
                ":" +
                time2string(timeSec);
    }

    public Player getPlayer() {
        return this.getPlayerData().getX();
    }

    public int getMapId() {
        return this.getPlayerData().getY();
    }

    public int getProgressId() {
        return progressId;
    }

    public void setProgressId(int progressId) {
        this.progressId = progressId;
    }

    public int getTimeId() {
        return timeId;
    }

    public void setTimeId(int timeId) {
        this.timeId = timeId;
    }

    private String time2string(int num) {
        return String.format("%02d", num);
    }
}
