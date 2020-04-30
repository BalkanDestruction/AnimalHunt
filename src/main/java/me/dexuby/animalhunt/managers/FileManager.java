package me.dexuby.animalhunt.managers;

import me.dexuby.animalhunt.AnimalHunt;
import me.dexuby.animalhunt.configuration.ConfigurationFile;
import me.dexuby.animalhunt.configuration.Settings;

import java.io.File;

public class FileManager {

    private static final AnimalHunt main = AnimalHunt.getInstance();
    public static final ConfigurationFile CONFIG = new ConfigurationFile(main, new File(main.getDataFolder(), "config.yml"));
    public static final ConfigurationFile LANG = new ConfigurationFile(main, new File(main.getDataFolder(), "lang.yml"));

    public FileManager() {

        CONFIG.addChild(Settings.getInstance());
        CONFIG.save();

    }

}
