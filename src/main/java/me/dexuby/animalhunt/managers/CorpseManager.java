package me.dexuby.animalhunt.managers;

import me.dexuby.animalhunt.CorpseData;
import org.bukkit.entity.Entity;

import java.util.*;

public class CorpseManager {

    private final WeakHashMap<Entity, CorpseData> deadBodyEntities = new WeakHashMap<>();

    public Set<Entity> getAllEntities() {

        Set<Entity> entities = new HashSet<>();
        for (Map.Entry<Entity, CorpseData> entry : deadBodyEntities.entrySet())
            entities.addAll(Arrays.asList(entry.getKey(), entry.getValue().getMountedEntity()));

        return entities;

    }

    public CorpseData getCorpseDataByEntity(final Entity entity) {

        return deadBodyEntities.get(entity);

    }

    public boolean isCorpseEntity(final Entity entity) {

        return deadBodyEntities.containsKey(entity);

    }

    public void addEntityCorpse(final Entity entity, final CorpseData corpseData) {

        deadBodyEntities.put(entity, corpseData);

    }

    public void removeEntityCorpse(final Entity entity) {

        deadBodyEntities.get(entity).getMountedEntity().remove();
        entity.remove();

    }

}
