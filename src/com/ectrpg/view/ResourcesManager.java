package com.ectrpg.view;

import org.frice.resource.image.FileImageResource;
import org.frice.resource.image.ImageResource;

import java.util.HashMap;
import java.util.Map;

public final class ResourcesManager {
    private ResourcesManager() {
    }

    private static Map<Integer, ImageResource> resources = new HashMap<>();

    public static ImageResource get(int i) throws RuntimeException {
        if (i < 0) {
            throw new RuntimeException();
        }
        if (resources.containsKey(i)) {
            return resources.get(i);
        }
        ImageResource ir = new FileImageResource("resources/assets/image/gui/dialog" + i + ".png");
        resources.put(i, ir);
        return ir;
    }

    public static void put(int i, ImageResource ir) {
        resources.put(i, ir);
    }
}
