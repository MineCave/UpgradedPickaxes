package com.minecave.pickaxes.skill.pick;

import com.minecave.pickaxes.EnhancedPicks;
import com.minecave.pickaxes.skill.PSkill;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Carter on 6/11/2015.
 */
public class Ice extends PSkill {

    private final int radius;

    public Ice(String name, long cooldown, int level, int cost, String perm, int radius) {
        super(name, cooldown, level, cost, perm);
        this.radius = radius;
    }

    @Override
    public void use(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        List<Block> blocks = getRegionBlocks(player.getLocation(), radius);

        for (Block block : blocks) {
            if (!this.wg.canBuild(event.getPlayer(), block)) {
                continue;
            }
            Collection<ItemStack> items = block.getDrops(player.getItemInHand());
            player.getInventory().addItem(items.toArray(new ItemStack[items.size()]));
            player.updateInventory();
            block.setType(Material.ICE);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                player.playSound(event.getPlayer().getLocation(), Sound.GLASS, 3.0F, 2.0F);
                for (Block block : blocks) {
                    if (!wg.canBuild(event.getPlayer(), block)) {
                        continue;
                    }
                    block.setType(Material.AIR);
                }
            }
        }.runTaskLater(EnhancedPicks.getInstance(), 50L);
        this.add(player);
    }

    public ArrayList<Block> getRegionBlocks(Location loc1, double radius) {
        ArrayList<Block> blocks = new ArrayList<>();
        for (double x = -radius; x <= radius; x++) {
            for (double y = -radius; y <= radius; y++) {
                for (double z = -radius; z <= radius; z++) {
                    Location l = loc1.clone().add(x, y, z);
                    if (!l.getChunk().isLoaded()) {
                        continue;
                    }
                    Block b = l.getBlock();
                    if (b.getType() != Material.AIR && b.getType().isBlock()) {
                        blocks.add(b);
                    }
                }
            }
        }
        return blocks;
    }

}