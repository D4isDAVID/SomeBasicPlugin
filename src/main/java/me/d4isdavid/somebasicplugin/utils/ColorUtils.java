package me.d4isdavid.somebasicplugin.utils;

import net.md_5.bungee.api.ChatColor;

public class ColorUtils {

    public static String t(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
