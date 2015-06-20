/*
 * Copyright (C) 2011-Current Richmond Steele (Not2EXceL) (nasm) <not2excel@gmail.com>
 * 
 * This file is part of minecave.
 * 
 * minecave can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */
package com.minecave.pickaxes.commands;

import com.minecave.pickaxes.PickaxesRevamped;
import com.minecave.pickaxes.items.ItemBuilder;
import com.minecave.pickaxes.pitem.PItemCreator;
import com.minecave.pickaxes.pitem.Pickaxe;
import com.minecave.pickaxes.pitem.Sword;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        PickaxesRevamped plugin = PickaxesRevamped.getInstance();
        if(strings.length < 2) {
            return false;
        }
        String typeStr = strings[0];
        String configStr = strings[1];
        if(!plugin.getPItemCreator().has(configStr)) {
            sender.sendMessage(ChatColor.RED + configStr + " does not exist in the config.");
            return true;
        }
        PItemCreator.PItemType type;
        if (typeStr.equalsIgnoreCase("pick") || typeStr.equalsIgnoreCase("p")) {
            type = PItemCreator.PItemType.PICK;
        } else if (typeStr.equalsIgnoreCase("sword") || typeStr.equalsIgnoreCase("s")) {
            type = PItemCreator.PItemType.SWORD;
        } else {
            return false;
        }
        PItemCreator.PItemSettings pItemSettings = plugin.getPItemCreator().get(configStr);
        if(type != pItemSettings.getType()) {
            sender.sendMessage(configStr + " is not of type " + type.name());
            return true;
        }
        Player player;
        if (strings.length == 2) {
            if (!(sender instanceof Player)) {
                return true;
            }
            player = (Player) sender;
            switch(type) {
                case PICK:
                    ItemBuilder builder = ItemBuilder.wrap(new ItemStack(Material.DIAMOND_PICKAXE));
                    Pickaxe pickaxe = pItemSettings.generate(builder.build(), Pickaxe.class);
                    player.getInventory().addItem(pickaxe.getItemStack());
                    pickaxe.update(player);
                    break;
                case SWORD:
                    builder = ItemBuilder.wrap(new ItemStack(Material.DIAMOND_SWORD));
                    Sword sword = pItemSettings.generate(builder.build(), Sword.class);
                    player.getInventory().addItem(sword.getItemStack());
                    sword.update(player);
                    break;
            }

        } else if (strings.length >= 3) {
            String playerStr = strings[2];
            player = Bukkit.getPlayer(playerStr);
            if(player == null || !player.isOnline()) {
                sender.sendMessage(ChatColor.RED + playerStr + " is not online.");
                return true;
            }
            switch(type) {
                case PICK:
                    ItemBuilder builder = ItemBuilder.wrap(new ItemStack(Material.DIAMOND_PICKAXE));
                    Pickaxe pickaxe = pItemSettings.generate(builder.build(), Pickaxe.class);
                    player.getInventory().addItem(pickaxe.getItemStack());
                    pickaxe.update(player);
                    break;
                case SWORD:
                    builder = ItemBuilder.wrap(new ItemStack(Material.DIAMOND_SWORD));
                    Sword sword = pItemSettings.generate(builder.build(), Sword.class);
                    player.getInventory().addItem(sword.getItemStack());
                    sword.update(player);
                    break;
            }
        }
        return true;
    }
}
