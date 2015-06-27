package com.minecave.pickaxes.listener;

import com.minecave.pickaxes.EnhancedPicks;
import com.minecave.pickaxes.drops.MobValue;
import com.minecave.pickaxes.item.PItem;
import com.minecave.pickaxes.player.PlayerInfo;
import com.minecave.pickaxes.skill.sword.Acid;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.List;
import java.util.UUID;

/**
 * @author Timothy Andis
 */
public class PlayerListener implements Listener {

    private EnhancedPicks plugin = EnhancedPicks.getInstance();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerInfo info = plugin.getPlayerManager().add(player);
        info.load();
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerInfo info = plugin.getPlayerManager().get(player);
        info.save();
        plugin.getPlayerManager().remove(player);
    }

    @EventHandler
    public void onProjectileHit(EntityDamageByEntityEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE &&
                !(event.getEntity() instanceof Player) &&
                event.getEntity() instanceof LivingEntity) {
            if (event.getDamager() instanceof Snowball) {
                Snowball entity = (Snowball) event.getDamager();
                if (entity.getCustomName().equals("shotgun")) {
                    event.getEntity().setMetadata("player", new FixedMetadataValue(plugin, entity.getMetadata("player")));
                    event.setDamage(((LivingEntity) event.getEntity()).getMaxHealth());
                }
            } else if (event.getDamager() instanceof EnderPearl) {
                EnderPearl entity = (EnderPearl) event.getDamager();
                if (entity.getCustomName().equals("acid")) {
                    event.getEntity().setMetadata("player", new FixedMetadataValue(plugin, entity.getMetadata("player")));
                    event.setDamage(((LivingEntity) event.getEntity()).getMaxHealth());
                }
            } else if(event.getDamager() instanceof Arrow) {
                if(event.getFinalDamage() >= ((LivingEntity) event.getEntity()).getHealth()) {
                    Arrow entity = (Arrow) event.getDamager();
                    event.getEntity().setMetadata("player", new FixedMetadataValue(plugin, entity.getMetadata("player")));
                }
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if(event.getEntity().hasMetadata("player")) {
            List<MetadataValue> m = event.getEntity().getMetadata("player");
            for(MetadataValue mv : m) {
                if(mv.value() instanceof String) {
                    String s = mv.asString();
                    Player player = Bukkit.getPlayer(UUID.fromString(s));
                    if(player != null) {
                        player.getInventory().addItem(event.getDrops().toArray(new ItemStack[event.getDrops().size()]));
                        PItem<?> pItem = plugin.getPItemManager().getPItem(player.getItemInHand());
                        if(pItem != null) {
                            int xp = MobValue.getXp(event.getEntityType());
                            pItem.incrementXp(xp, player);
                        }
                        event.getEntity().remove();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onProjectileLand(ProjectileHitEvent event) {
        if (event.getEntity() instanceof EnderPearl) {
            EnderPearl entity = (EnderPearl) event.getEntity();
            if (entity.getCustomName().equals("acid")) {
                int radius = plugin.getPSkillManager().getPSkill(Acid.class).getRadius();
                entity.getNearbyEntities(radius, radius, radius).stream()
                        .filter(e -> !(e instanceof Player) && e instanceof LivingEntity)
                        .forEach(e -> {
                            LivingEntity le = (LivingEntity) e;
                            event.getEntity().setMetadata("player", new FixedMetadataValue(plugin, entity.getMetadata("player")));
                            le.damage(((LivingEntity) event.getEntity()).getMaxHealth());

                        });
            }
        }
        if (event.getEntity() instanceof Fireball) {
            Fireball entity = (Fireball) event.getEntity();
            if (entity.getCustomName().equals("fireball")) {
                entity.getWorld().createExplosion(entity.getLocation(), 2.0f, false);
            }
        }
    }

    @EventHandler
    public void onEntityTeleport(PlayerTeleportEvent event) {
        if(event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            PItem<?> pItem = plugin.getPItemManager().getPItem(event.getPlayer().getItemInHand());
            if(pItem != null) {
                event.setCancelled(true);
            }
        }
    }
}
