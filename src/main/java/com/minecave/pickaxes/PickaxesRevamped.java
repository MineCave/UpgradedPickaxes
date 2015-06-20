package com.minecave.pickaxes;

import com.minecave.pickaxes.commands.GiveCommand;
import com.minecave.pickaxes.commands.PickCommand;
import com.minecave.pickaxes.commands.SwordCommand;
import com.minecave.pickaxes.config.ConfigValues;
import com.minecave.pickaxes.drops.BlockValues;
import com.minecave.pickaxes.enchant.enchants.LuckEnchant;
import com.minecave.pickaxes.enchant.enchants.NormalEnchant;
import com.minecave.pickaxes.enchant.enchants.TnTEnchant;
import com.minecave.pickaxes.items.GlowEnchant;
import com.minecave.pickaxes.listener.MenuListener;
import com.minecave.pickaxes.listener.PItemListener;
import com.minecave.pickaxes.listener.PlayerListener;
import com.minecave.pickaxes.pitem.PItem;
import com.minecave.pickaxes.pitem.PItemCreator;
import com.minecave.pickaxes.pitem.Pickaxe;
import com.minecave.pickaxes.pitem.Sword;
import com.minecave.pickaxes.skill.Skills;
import com.minecave.pickaxes.sql.PlayerInfo;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Carter on 6/4/2015.
 */
public class PickaxesRevamped extends JavaPlugin {

    private static PickaxesRevamped instance;
    private ConfigValues configValues;
    @Getter
    private PItemCreator pItemCreator;

    public static PickaxesRevamped getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        Skills.skills = new HashMap<>();
        saveDefaultConfig();

        attemptSaveResource("drops.yml");
        attemptSaveResource("levels.yml");
        attemptSaveResource("xp.yml");
        attemptSaveResource("sql.yml");
        attemptSaveResource("menus.yml");
        attemptSaveResource("skills.yml");
        attemptSaveResource("enchants.yml");

        this.configValues = new ConfigValues(getConfig());
        configValues.init();
        new PItemListener();
        new PlayerListener();
        new MenuListener();
        getCommand("pick").setExecutor(new PickCommand());
        getCommand("sword").setExecutor(new SwordCommand());
        getCommand("pGive").setExecutor(new GiveCommand());
        GlowEnchant.register();

        FileConfiguration config = PickaxesRevamped.getInstance().getConfigValues().getEnchants();
        List<String> enchants = config.getStringList("availableEnchants");
        for (String s : enchants) {
            switch (s) {
                case "tnt":
                    PItem.getEnchantMap().put(s, new TnTEnchant());
                    break;
                case "luck":
                    PItem.getEnchantMap().put(s, new LuckEnchant());
                    break;
                default:
                    if (NormalEnchant.VanillaPick.has(s)) {
                        NormalEnchant.VanillaPick pick = NormalEnchant.VanillaPick.valueOf(s.toUpperCase());
                        if (pick != null) {
                            NormalEnchant base = new NormalEnchant(pick.getEnchantment());
                            PItem.getEnchantMap().put(s, base);
                            if(s.contains("_")) {
                                PItem.getEnchantMap().put(s.replace("_", " "), base);
                                PItem.getEnchantMap().put(s.replace("_", ""), base);
                            }
                        }
                    } else if (NormalEnchant.VanillaSword.has(s)) {
                        NormalEnchant.VanillaSword sword = NormalEnchant.VanillaSword.valueOf(s.toUpperCase());
                        if (sword != null) {
                            NormalEnchant base = new NormalEnchant(sword.getEnchantment());
                            PItem.getEnchantMap().put(s, base);
                            if(s.contains("_")) {
                                PItem.getEnchantMap().put(s.replace("_", " "), base);
                                PItem.getEnchantMap().put(s.replace("_", ""), base);
                            }
                        }
                    }
            }
        }
        pItemCreator = new PItemCreator();

        Bukkit.getOnlinePlayers().forEach(PlayerInfo::init);


    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(PlayerInfo::save);
        Skills.skills.clear();
        PlayerInfo.getInfoMap().clear();
        BlockValues.values.clear();
        Pickaxe.getPickaxeList().clear();
        Sword.getSwordList().clear();
        PItem.getEnchantMap().clear();
    }

    public void attemptSaveResource(String name) {
        if(!(new File(getDataFolder(), name).exists())) {
            saveResource(name, false);
        }
    }

    public ConfigValues getConfigValues() {
        return configValues;
    }
}
