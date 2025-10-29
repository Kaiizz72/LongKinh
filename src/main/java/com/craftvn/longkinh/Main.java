package com.craftvn.longkinh;

import org.bukkit.plugin.java.JavaPlugin;
import com.craftvn.longkinh.commands.MuaLongCommand;
import com.craftvn.longkinh.listeners.SkillListener;
import com.craftvn.longkinh.listeners.CageProtectionListener;
import com.craftvn.longkinh.utils.CageManager;
import com.craftvn.longkinh.utils.ItemManager;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        CageManager cageManager = new CageManager(this);
        ItemManager itemManager = new ItemManager();

        getCommand("mualong").setExecutor(new MuaLongCommand(this, itemManager));
        getServer().getPluginManager().registerEvents(new SkillListener(cageManager), this);
        getServer().getPluginManager().registerEvents(new CageProtectionListener(cageManager), this);

        getLogger().info("LongKinh enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("LongKinh disabled.");
    }
}
