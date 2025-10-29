package com.craftvn.longkinh.listeners;

import com.craftvn.longkinh.utils.CageManager;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class CageProtectionListener implements Listener {

    private final CageManager manager;
    public CageProtectionListener(CageManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (manager.isProtected(e.getBlock())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "Không thể phá Lồng Kính!");
        }
    }
}
