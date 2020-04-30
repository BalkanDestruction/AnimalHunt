package me.dexuby.animalhunt.configuration;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class ConfigurationFile {

    private final JavaPlugin main;
    private final File file;
    private FileConfiguration config;
    private final Collection<ConfigurationOptionHolder> children = new ArrayList<>();

    public ConfigurationFile(final JavaPlugin main,
                             final File file) {

        this.main = main;
        this.file = file;

        if (!file.exists()) saveResource();
        config = YamlConfiguration.loadConfiguration(file);

    }

    public void reload() {

        if (file.exists()) config = YamlConfiguration.loadConfiguration(file);

    }

    private void saveResource() {

        try {
            main.saveResource(file.getName(), false);
        } catch (Exception ex) {
            main.getLogger().warning("Failed to save resource: " + file.getAbsolutePath());
        }

    }

    public void save() {

        try {
            config.save(file);
        } catch (Exception ex) {
            main.getLogger().warning("Failed to save file: " + file.getAbsolutePath());
        }

    }

    public ConfigurationFile addChild(ConfigurationOptionHolder holder) {

        children.add(holder);
        return this;

    }

    public FileConfiguration getConfig() {

        return config;

    }

    public Collection<ConfigurationOptionHolder> getChildren() {

        return children;

    }

}
