/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.supermart.util;

/**
 *
 * @author pabodini
 */
import java.util.prefs.Preferences;

public final class AppPreferences {

    private static final Preferences PREFS =
            Preferences.userNodeForPackage(
                    AppPreferences.class
            );

    private static final String KEY_THEME =
            "ui.theme";

    private static final String KEY_FONT_SIZE =
            "ui.fontSize";

    public static final String THEME_LIGHT =
            "LIGHT";

    public static final String THEME_DARK =
            "DARK";

    public static final int DEFAULT_FONT_SIZE = 16;

    public static final int MIN_FONT_SIZE = 12;

    public static final int MAX_FONT_SIZE = 22;


    private AppPreferences() {
    }


    public static String getTheme() {

        return PREFS.get(
                KEY_THEME,
                THEME_LIGHT
        );
    }


    public static void setTheme(
            String theme
    ) {

        if (!THEME_LIGHT.equals(theme)
                && !THEME_DARK.equals(theme)) {

            theme = THEME_LIGHT;
        }

        PREFS.put(
                KEY_THEME,
                theme
        );
    }


    public static int getFontSize() {

        int size =
                PREFS.getInt(
                        KEY_FONT_SIZE,
                        DEFAULT_FONT_SIZE
                );

        return Math.max(
                MIN_FONT_SIZE,
                Math.min(
                        MAX_FONT_SIZE,
                        size
                )
        );
    }


    public static void setFontSize(
            int size
    ) {

        int safeSize =
                Math.max(
                        MIN_FONT_SIZE,
                        Math.min(
                                MAX_FONT_SIZE,
                                size
                        )
                );

        PREFS.putInt(
                KEY_FONT_SIZE,
                safeSize
        );
    }


    public static void resetAppearance() {

        PREFS.put(
                KEY_THEME,
                THEME_LIGHT
        );

        PREFS.putInt(
                KEY_FONT_SIZE,
                DEFAULT_FONT_SIZE
        );
    }
}