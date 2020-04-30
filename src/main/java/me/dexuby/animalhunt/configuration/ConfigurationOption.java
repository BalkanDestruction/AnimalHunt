package me.dexuby.animalhunt.configuration;

import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class ConfigurationOption {

    private final ConfigurationFile file;
    private final String path;

    ConfigurationOption(final ConfigurationFile file, final String path, final Object def) {

        this.file = file;
        this.path = path;

        if (!file.getConfig().isSet(path)) file.getConfig().set(path, def);

    }

    public String getPath() {

        return path;

    }

    public boolean getBoolean() {

        return file.getConfig().getBoolean(path);

    }

    public boolean getBoolean(boolean def) {

        return file.getConfig().getBoolean(path, def);

    }

    public Color getColor() {

        return file.getConfig().getColor(path);

    }

    public Color getColor(Color def) {

        return file.getConfig().getColor(path, def);

    }

    public double getDouble() {

        return file.getConfig().getDouble(path);

    }

    public double getDouble(double def) {

        return file.getConfig().getDouble(path, def);

    }

    public int getInt() {

        return file.getConfig().getInt(path);

    }

    public int getInt(int def) {

        return file.getConfig().getInt(path, def);

    }

    public ItemStack getItemStack() {

        return file.getConfig().getItemStack(path);

    }

    public ItemStack getItemStack(ItemStack def) {

        return file.getConfig().getItemStack(path, def);

    }

    public long getLong() {

        return file.getConfig().getLong(path);

    }

    public long getLong(long def) {

        return file.getConfig().getLong(path, def);

    }

    public String getString() {

        return file.getConfig().getString(path);

    }

    public String getString(String def) {

        return file.getConfig().getString(path, def);

    }

    public List<Integer> getIntegerList() {

        return file.getConfig().getIntegerList(path);

    }

    public List<String> getStringList() {

        return file.getConfig().getStringList(path);

    }

    public <T> T getObject(Class<T> clazz) {

        return file.getConfig().getObject(path, clazz);

    }

    public <T> T getObject(Class<T> clazz, T def) {

        return file.getConfig().getObject(path, clazz, def);

    }

    public <T extends Enum<T>> T getEnum(Class<T> enumClass) {

        return Enum.valueOf(enumClass, getString(path));

    }

    public <T extends Enum<T>> List<T> getEnumList(Class<T> enumClass) {

        return getStringList().stream().map(entry -> getEnum(enumClass, entry)).collect(Collectors.toList());

    }

    private <T extends Enum<T>> T getEnum(Class<T> enumClass, String value) {

        return Enum.valueOf(enumClass, value);

    }

}
