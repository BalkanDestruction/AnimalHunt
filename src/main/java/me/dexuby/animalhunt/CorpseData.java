package me.dexuby.animalhunt;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class CorpseData {

    private final Entity mountedEntity;
    private final Collection<ItemStack> drops;

    public CorpseData(final Entity mountedEntity,
                      final Collection<ItemStack> drops) {

        this.mountedEntity = mountedEntity;
        this.drops = drops;

    }

    public Entity getMountedEntity() {

        return mountedEntity;

    }

    public Collection<ItemStack> getDrops() {

        return drops;

    }

}
