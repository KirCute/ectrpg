package com.ectrpg.db;

import org.frice.util.data.Database;
import org.frice.util.data.Preference;

public class Localization {
    private static String font;
    private static Database text;
    public static void initText(String font, String lang) {
        Localization.font = font;
        text = new Preference("resources/assets/lang/" + lang + ".lang");
    }
    public static String query(String key) {
        return text.queryT(key, "");
    }

    public static String getFont() {
        return font;
    }
}
