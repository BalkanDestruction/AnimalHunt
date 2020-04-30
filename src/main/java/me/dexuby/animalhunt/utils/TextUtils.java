package me.dexuby.animalhunt.utils;

import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

public class TextUtils {

    public static List<String> translateAlternateColorCodes(char altColorCode, List<String> listToTranslate) {

        return listToTranslate.stream().map(entry -> ChatColor.translateAlternateColorCodes(altColorCode, entry)).collect(Collectors.toList());

    }

}
