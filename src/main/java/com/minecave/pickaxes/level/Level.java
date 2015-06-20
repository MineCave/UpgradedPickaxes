/*
 * Copyright (C) 2011-Current Richmond Steele (Not2EXceL) (nasm) <not2excel@gmail.com>
 * 
 * This file is part of minecave.
 * 
 * minecave can not be copied and/or distributed without the express
 * permission of the aforementioned owner.
 */
package com.minecave.pickaxes.level;

import com.minecave.pickaxes.EnhancedPicks;
import com.minecave.pickaxes.item.PItem;
import com.minecave.pickaxes.util.firework.FireworkBuilder;
import com.minecave.pickaxes.util.message.MessageBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Level {

    private final LevelManager manager;
    @Getter
    private int id;
    @Getter
    private int xp;
    @Getter
    private List<String> commands;
    @Getter
    private FireworkBuilder fireworkBuilder;

    public Level(int id, int xp, List<String> commands, FireworkBuilder fireworkBuilder) {
        manager = EnhancedPicks.getInstance().getLevelManager();
        this.id = id;
        this.xp = xp;
        this.commands = commands;
        this.fireworkBuilder = fireworkBuilder;
    }

    public void levelUp(Player player, PItem pItem) {
        Level next = getNext();
        List<String> messages = new ArrayList<>();
        for (String s : manager.getLevelUpMessage()) {
            MessageBuilder builder = new MessageBuilder(s);
            builder.replace(player)
                    .replace(id, MessageBuilder.IntegerType.PLAYER_LEVEL);
            if(next.id < pItem.getMaxLevel()) {
                builder.replace(next.xp, MessageBuilder.IntegerType.NEXT_XP)
                        .replace(next.id, MessageBuilder.IntegerType.NEXT_LEVEL);
            } else {
                builder.replace("N/A", MessageBuilder.IntegerType.NEXT_XP)
                        .replace("Max Level", MessageBuilder.IntegerType.NEXT_LEVEL);
            }
            builder.replace(0, MessageBuilder.IntegerType.XP)
                    .replace(pItem);
            messages.add(builder.build());
        }
        if (!manager.getBlackList().contains(this.id)) {

        }
        for (String s : this.commands) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s);
        }
        player.sendMessage(messages.toArray(new String[messages.size()]));
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
        if (fireworkBuilder != null) {
            fireworkBuilder.play(player);
        }
    }

    public Level getPrevious() {
        return manager.getLevel(id - 1);
    }

    public Level getNext() {
        return manager.getLevel(id + 1);
    }
}