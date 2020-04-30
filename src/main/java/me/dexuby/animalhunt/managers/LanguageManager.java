package me.dexuby.animalhunt.managers;

import me.dexuby.animalhunt.configuration.ConfigurationFile;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class LanguageManager {

    private final ConfigurationFile lang = FileManager.LANG;
    private final Map<String, String> stringCacheMap = new HashMap<>();
    private final Map<String, List<String>> stringListCacheMap = new HashMap<>();

    public LanguageManager() {

        reload();

    }

    public void reload() {

        stringCacheMap.clear();
        stringListCacheMap.clear();
        lang.reload();

        FileConfiguration config = lang.getConfig();
        for (String key : config.getKeys(false)) {
            if (config.isList(key)) {
                final List<String> stringList = new ArrayList<>();
                config.getStringList(key).forEach(string -> stringList.add(ChatColor.translateAlternateColorCodes('&', string)));
                stringListCacheMap.put(key, stringList);
            } else if (config.isString(key)) {
                stringCacheMap.put(key, ChatColor.translateAlternateColorCodes('&', config.getString(key)));
            }
        }

    }

    public String getString(final String key) {

        return stringCacheMap.getOrDefault(key, key);

    }

    public List<String> getStringList(final String key) {

        return stringListCacheMap.getOrDefault(key, Collections.singletonList(key));

    }

}
