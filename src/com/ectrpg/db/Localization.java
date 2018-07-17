package com.ectrpg.db;

import org.frice.util.data.Database;
import org.frice.util.data.Preference;

public class Localization {
    private static String font;
    private static Database text;
    private static int wordWidth;
    public static void initText(String font, String lang) {
        Localization.font = font;
        text = new Preference("resources/assets/lang/" + lang + ".lang");
        wordWidth = text.queryT("lang.width", 1);
    }
    public static String query(String key) {
        return text.queryT(key, "");
    }

    public static int getWidth() {
        return wordWidth;
    }

    public static String getFont() {
        return font;
    }
}
