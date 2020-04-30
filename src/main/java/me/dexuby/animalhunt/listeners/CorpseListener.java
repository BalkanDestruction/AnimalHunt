package me.dexuby.animalhunt.listeners;

import me.dexuby.animalhunt.AnimalHunt;
import me.dexuby.animalhunt.CorpseData;
import me.dexuby.animalhunt.configuration.Settings;
import me.dexuby.animalhunt.managers.CorpseManager;
import me.dexuby.animalhunt.managers.LanguageManager;
import me.dexuby.animalhunt.ActivationInput;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.Lootable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;

import java.util.*;

@SuppressWarnings("deprecation")
public class CorpseListener implements Listener {

    private final AnimalHunt main;
    private final LanguageManager languageManager;
    private final CorpseManager corpseManager;
    private final Set<UUID> butcheringPlayers = new HashSet<>();
    private final Random random = new Random();

    public CorpseListener(final AnimalHunt main) {

        this.main = main;
        this.languageManager = main.getLanguageManager();
        this.corpseManager = main.getCorpseManager();

    }

    @EventHandler
    public void onEntityDeath(final EntityDeathEvent event) {

        if (event.getEntity() instanceof Animals) {
            final LivingEntity entity = event.getEntity();
            if (corpseManager.isCorpseEntity(entity)) {
                event.getDrops().clear();
                event.setDroppedExp(0);
                corpseManager.removeEntityCorpse(entity);
            }
        }

    }

    @EventHandler
    public void onEntityDamage(final EntityDamageEvent event) {

        if (event.getEntity() instanceof Animals) {
            final LivingEntity entity = (LivingEntity) event.getEntity();
            if (corpseManager.isCorpseEntity(entity)) event.setCancelled(true);
            if (event.getFinalDamage() >= entity.getHealth()) {
                if (Settings.WORLD_NAME_WHITELIST.getStringList().size() > 0 &&
                        !Settings.WORLD_NAME_WHITELIST.getStringList().contains(entity.getWorld().getName())) {
                    return;
                }

                if (Settings.WORLD_NAME_BLACKLIST.getStringList().contains(entity.getWorld().getName()))
                    return;

                if (Settings.ENTITY_TYPE_WHITELIST.getStringList().size() > 0 &&
                        !Settings.ENTITY_TYPE_WHITELIST.getEnumList(EntityType.class).contains(entity.getType())) {
                    return;
                }

                if (Settings.ENTITY_TYPE_BLACKLIST.getEnumList(EntityType.class).contains(entity.getType()))
                    return;

                if (!corpseManager.isCorpseEntity(entity)) {
                    event.setCancelled(true);
                    spawnDeadBodyEntity(entity, getLoot(entity, event));
                }
            }
        }

    }

    private Collection<ItemStack> getLoot(final LivingEntity entity, final EntityDamageEvent event) {

        if (!(entity instanceof Lootable)) return null;
        final LootTable lootTable = ((Lootable) entity).getLootTable();
        if (lootTable == null) return null;
        if (event instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) event).getDamager() instanceof Player) {
            final EntityDamageByEntityEvent eveEvent = (EntityDamageByEntityEvent) event;
            final Player player = (Player) eveEvent.getDamager();
            int luck, lootingModifier = 0;
            final PotionEffect potionEffect = player.getPotionEffect(PotionEffectType.LUCK);
            luck = potionEffect != null ? potionEffect.getAmplifier() + 1 : 0;
            final EntityEquipment equipment = player.getEquipment();
            if (equipment != null && equipment.getItemInMainHand().getType() != Material.AIR) {
                final ItemStack mainHandItem = equipment.getItemInMainHand();
                if (mainHandItem.getItemMeta() != null && mainHandItem.getItemMeta().hasEnchant(Enchantment.LOOT_BONUS_MOBS))
                    lootingModifier = mainHandItem.getItemMeta().getEnchantLevel(Enchantment.LOOT_BONUS_MOBS);
            }
            return lootTable.populateLoot(random,
                    new LootContext.Builder(entity.getLocation())
                            .lootingModifier(lootingModifier)
                            .luck(luck)
                            .lootedEntity(entity)
                            .killer(player)
                            .build());
        } else {
            return lootTable.populateLoot(random,
                    new LootContext.Builder(entity.getLocation())
                            .lootedEntity(entity)
                            .build());
        }

    }

    private void spawnDeadBodyEntity(final LivingEntity entity, final Collection<ItemStack> drops) {

        entity.setCustomName("Dinnerbone");
        entity.setCustomNameVisible(false);
        entity.setInvulnerable(true);
        entity.setSilent(true);
        entity.setGravity(true);
        entity.setRemoveWhenFarAway(false);
        entity.setPersistent(false);

        // AVOIDS FLOATING ENTITIES
        if (entity.isOnGround()) {
            entity.setAI(false);
        } else {
            final int[] counter = {0};
            new BukkitRunnable() {
                @Override
                public void run() {
                    counter[0]++;
                    if (counter[0] >= 20 || !entity.isValid()) {
                        corpseManager.removeEntityCorpse(entity);
                        cancel();
                    } else {
                        if (entity.isOnGround()) {
                            entity.setAI(false);
                            cancel();
                        }
                    }
                }
            }.runTaskTimerAsynchronously(main, 0L, 10L);
        }

        final ArmorStand passengerEntity = (ArmorStand) entity.getWorld().spawnEntity(entity.getLocation(), EntityType.ARMOR_STAND);
        passengerEntity.setVisible(false);
        passengerEntity.setBasePlate(false);
        passengerEntity.setSmall(true);
        passengerEntity.setMarker(true);
        passengerEntity.setInvulnerable(true);
        passengerEntity.setSilent(true);
        passengerEntity.setRemoveWhenFarAway(false);
        passengerEntity.setPersistent(false);

        corpseManager.addEntityCorpse(entity, new CorpseData(passengerEntity, drops));
        main.getServer().getScheduler().runTaskLater(main, () -> entity.addPassenger(passengerEntity), 1L); // GLITCH WITHOUT DELAY

    }

    @EventHandler
    public void onPlayerSwapHandItems(final PlayerSwapHandItemsEvent event) {

        if (Settings.ACTIVATION_INPUT.getEnum(ActivationInput.class) == ActivationInput.WEAPON_SWAP)
            startButcheringAttempt(event, event.getPlayer());

    }

    @EventHandler
    public void onPlayerToggleSneak(final PlayerToggleSneakEvent event) {

        if (event.isSneaking()) {
            if (Settings.ACTIVATION_INPUT.getEnum(ActivationInput.class) == ActivationInput.SNEAK)
                startButcheringAttempt(event, event.getPlayer());
        }

    }

    @EventHandler
    public void onPlayerInteractEntity(final PlayerInteractEntityEvent event) {

        if (corpseManager.isCorpseEntity(event.getRightClicked())) {
            event.setCancelled(true);
            if (Settings.ACTIVATION_INPUT.getEnum(ActivationInput.class) == ActivationInput.RIGHT_CLICK) {
                startButcheringAttempt(event, event.getPlayer());
            }
        }

    }

    private void startButcheringAttempt(final Cancellable event, final Player player) {

        if (!player.hasPermission("animalhunt.butcher")) return;
        if (Settings.GAME_MODE_WHITELIST.getStringList().size() > 0 &&
                !Settings.GAME_MODE_WHITELIST.getEnumList(GameMode.class).contains(player.getGameMode())) {
            return;
        }
        if (Settings.GAME_MODE_BLACKLIST.getEnumList(GameMode.class).contains(player.getGameMode()))
            return;
        final EntityEquipment playerEquipment = player.getEquipment();
        if (playerEquipment != null) {
            final Material itemInMainHandMaterial = playerEquipment.getItemInMainHand().getType();
            if (Settings.ITEM_IN_HAND_WHITELIST.getStringList().size() > 0 &&
                    !Settings.ITEM_IN_HAND_WHITELIST.getEnumList(Material.class).contains(itemInMainHandMaterial)) {
                return;
            }

            if (Settings.ITEM_IN_HAND_BLACKLIST.getEnumList(Material.class).contains(itemInMainHandMaterial))
                return;
        }

        final Entity entity = getPlayerTargetEntity(player);
        if (entity instanceof LivingEntity && corpseManager.isCorpseEntity(entity)) {
            if (butcheringPlayers.contains(player.getUniqueId())) return;
            butcheringPlayers.add(player.getUniqueId());
            event.setCancelled(true);
            final int entityId = entity.getEntityId();
            final int[] counter = {0};
            new BukkitRunnable() {
                @Override
                public void run() {
                    final Entity targetEntity = getPlayerTargetEntity(player);
                    if (targetEntity != null && targetEntity.getEntityId() == entityId) {
                        counter[0]++;
                        if (counter[0] >= 8) {
                            butcheringPlayers.remove(player.getUniqueId());
                            player.sendTitle(languageManager.getString("finished-butchering-title"), languageManager.getString("finished-butchering-subtitle"));
                            corpseManager.getCorpseDataByEntity(targetEntity).getDrops()
                                    .forEach(itemStack -> player.getWorld().dropItemNaturally(targetEntity.getLocation(), itemStack));
                            corpseManager.removeEntityCorpse(targetEntity);
                            cancel();
                        } else {
                            player.sendTitle(languageManager.getString("ongoing-butchering-title"), generateProgressBar(counter[0], 8), 0, 20, 0);
                        }
                    } else {
                        butcheringPlayers.remove(player.getUniqueId());
                        player.sendTitle(languageManager.getString("interrupted-butchering-title"), languageManager.getString("interrupted-butchering-subtitle"));
                        cancel();
                    }
                }
            }.runTaskTimer(main, 0L, 10L);
        }

    }

    private Entity getPlayerTargetEntity(final Player player) {

        final Location eyeLocation = player.getEyeLocation().clone();
        final RayTraceResult rayTraceResult = player.getWorld().rayTraceEntities(eyeLocation.add(eyeLocation.getDirection()), eyeLocation.getDirection(), Settings.MAX_DISTANCE.getDouble());
        if (rayTraceResult != null && rayTraceResult.getHitEntity() != null)
            return rayTraceResult.getHitEntity();
        else
            return null;

    }

    private String generateProgressBar(final int current, final int max) {

        String filledPart = languageManager.getString("progress-filled-part");
        String emptyPart = languageManager.getString("progress-empty-part");

        int bars = (int) Math.round(((double) current / max) * 10);
        StringBuilder progressBarBuilder = new StringBuilder();
        for (int i = 0; i <= 10; i++)
            progressBarBuilder.append(i <= bars ? filledPart : emptyPart);

        return progressBarBuilder.toString();

    }

    @EventHandler
    public void onChunkUnload(final ChunkUnloadEvent event) {

        if (Settings.REMOVE_CORPSES_ON_CHUNK_UNLOAD.getBoolean()) {
            for (Entity entity : event.getChunk().getEntities()) {
                if (corpseManager.isCorpseEntity(entity))
                    corpseManager.removeEntityCorpse(entity);
            }
        }

    }

}
