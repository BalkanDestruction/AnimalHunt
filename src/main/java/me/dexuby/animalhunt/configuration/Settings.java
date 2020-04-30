package me.dexuby.animalhunt.configuration;

import me.dexuby.animalhunt.managers.FileManager;
import me.dexuby.animalhunt.ActivationInput;

import java.util.ArrayList;

public class Settings extends ConfigurationOptionHolder {

    private static Settings instance;

    private Settings() {
    }

    private static final ConfigurationFile file = FileManager.CONFIG;
    public static final ConfigurationOption ACTIVATION_INPUT = new ConfigurationOption(file, "activation-input", ActivationInput.WEAPON_SWAP.name());
    public static final ConfigurationOption WORLD_NAME_WHITELIST = new ConfigurationOption(file, "world-name-whitelist", new ArrayList<>());
    public static final ConfigurationOption WORLD_NAME_BLACKLIST = new ConfigurationOption(file, "world-name-blacklist", new ArrayList<>());
    public static final ConfigurationOption GAME_MODE_WHITELIST = new ConfigurationOption(file, "game-mode-whitelist", new ArrayList<>());
    public static final ConfigurationOption GAME_MODE_BLACKLIST = new ConfigurationOption(file, "game-mode-blacklist", new ArrayList<>());
    public static final ConfigurationOption ENTITY_TYPE_WHITELIST = new ConfigurationOption(file, "entity-type-whitelist", new ArrayList<>());
    public static final ConfigurationOption ENTITY_TYPE_BLACKLIST = new ConfigurationOption(file, "entity-type-blacklist", new ArrayList<>());
    public static final ConfigurationOption ITEM_IN_HAND_WHITELIST = new ConfigurationOption(file, "item-in-hand-whitelist", new ArrayList<>());
    public static final ConfigurationOption ITEM_IN_HAND_BLACKLIST = new ConfigurationOption(file, "item-in-hand-blacklist", new ArrayList<>());
    public static final ConfigurationOption REMOVE_CORPSES_ON_CHUNK_UNLOAD = new ConfigurationOption(file, "remove-corpses-on-chunk-unload", true);
    public static final ConfigurationOption MAX_DISTANCE = new ConfigurationOption(file, "max-distance", 0.8);

    public static Settings getInstance() {

        if (instance == null)
            instance = new Settings();
        return instance;

    }

}
