package me.dexuby.animalhunt;

import me.dexuby.animalhunt.listeners.CorpseListener;
import me.dexuby.animalhunt.managers.CorpseManager;
import me.dexuby.animalhunt.managers.FileManager;
import me.dexuby.animalhunt.managers.LanguageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class AnimalHunt extends JavaPlugin implements CommandExecutor {

    private static AnimalHunt main;

    private final PluginManager pluginManager = getServer().getPluginManager();
    private FileManager fileManager;
    private LanguageManager languageManager;
    private CorpseManager corpseManager;

    @Override
    public void onLoad() {

        main = this;

    }

    @Override
    public void onEnable() {

        registerManagers();
        registerListeners();

        Objects.requireNonNull(getCommand("animalhunt")).setExecutor(this);

    }

    @Override
    public void onDisable() {

        corpseManager.getAllEntities().forEach(Entity::remove);

    }

    private void registerManagers() {

        fileManager = new FileManager();
        languageManager = new LanguageManager();
        corpseManager = new CorpseManager();

    }

    private void registerListeners() {

        pluginManager.registerEvents(new CorpseListener(this), this);

    }

    public boolean onCommand(final CommandSender sender, final Command cmd, final String cmdName, final String[] args) {

        String subCommand = args.length > 0 ? args[0] : "help";
        if (subCommand.equalsIgnoreCase("reload")) {
            if (sender.hasPermission("animalhunt.reload")) {
                FileManager.CONFIG.reload();
                languageManager.reload();
                sender.sendMessage(languageManager.getString("files-reloaded"));
            } else {
                sender.sendMessage(languageManager.getString("command-no-permission"));
            }
        } else if (subCommand.equalsIgnoreCase("help")) {
            if (sender.hasPermission("animalhunt.help")) {
                languageManager.getStringList("help").forEach(sender::sendMessage);
            } else {
                sender.sendMessage(languageManager.getString("command-no-permission"));
            }
        } else {
            sender.sendMessage(languageManager.getString("command-not-found"));
        }

        return true;

    }

    public LanguageManager getLanguageManager() {

        return languageManager;

    }

    public CorpseManager getCorpseManager() {

        return corpseManager;

    }

    public static AnimalHunt getInstance() {

        return main;

    }

}
