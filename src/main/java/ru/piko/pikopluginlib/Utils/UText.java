package ru.piko.allpikoplugin.Utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class UText {

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static List<String> color(List<String> list) {
        List<String> result = new ArrayList<>();
        for (String string: list) {
            result.add(color(string));
        }
        return result;
    }


}
