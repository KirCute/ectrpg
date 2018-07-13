package com.ectrpg.main.maker;

import com.ectrpg.model.LocationPair;
import com.ectrpg.model.map.Maps;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.BitSet;

public final class MapMaker {
    private MapMaker() {
    }
    public static void main(String[] args) {
        int mapId = 0;
        int width = 19;
        int brightness = -1;
        boolean[] mapBlock = {
                true ,true ,true ,true ,true ,true ,true ,true ,true ,false,true ,true ,true ,true ,true ,true ,true ,true ,true ,
                true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true ,
                true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true ,
                true ,false,false,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,false,false,true ,
                true ,false,false,true ,false,false,false,false,false,false,false,false,false,false,false,true ,false,false,true ,
                true ,false,false,true ,false,false,false,false,false,false,false,false,false,false,false,true ,false,false,true ,
                true ,false,false,true ,false,false,false,false,false,false,false,false,false,false,false,true ,false,false,true ,
                true ,false,false,true ,false,false,false,false,false,false,false,false,false,false,false,true ,false,false,true ,
                true ,false,false,true ,false,false,false,false,false,false,false,false,false,false,false,true ,false,false,true ,
                true ,false,false,true ,false,false,false,false,false,false,false,false,false,false,false,true ,false,false,true ,
                true ,false,false,true ,false,false,false,false,false,false,false,false,false,false,false,true ,false,false,true ,
                true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true ,
                true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true ,
                true ,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,true ,
                true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,true ,
        };
        //ArrayList<Item> item = new ArrayList<>();
        //item.add(new Door("TestDoor", new LocationPair<>(9, 0) ,new Pair<>(1, new LocationPair<>(2.0F,3.0F)), Entity.TOWARD_UP));
        LocationPair<Integer> viewShifting = new LocationPair<>(0,0);
        /*
        int mapId = 1;
        int width = 5;
        int brightness = -1;
        boolean[] mapBlock = {
                true ,true ,true ,true ,true ,
                true ,false,false,false,true ,
                true ,false,false,false,true ,
                true ,false,false,false,true ,
                true ,true ,false,true ,true
        };
        //ArrayList<Item> item = new ArrayList<>();
        //item.add(new Door("TestDoor", new LocationPair<>(2, 4), new Pair<>(0, new LocationPair<>(9.0F,1.0F)), Entity.TOWARD_DOWN));
        LocationPair<Integer> viewShifting = new LocationPair<>(0,0);
        */

        BitSet bs = new BitSet();
        for(int i = 0; i < mapBlock.length; i++) {
            bs.set(i, mapBlock[i]);
        }
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("resources/object/map", "map" + String.format("%03d", mapId) + ".map")));
            oos.writeObject(new Maps(mapId, width, brightness, bs, viewShifting));
            oos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
