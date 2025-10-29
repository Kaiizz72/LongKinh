package com.craftvn.longkinh.commands;

import com.craftvn.longkinh.utils.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MuaLongCommand implements CommandExecutor, Listener {

    private final ItemManager itemManager;

    public MuaLongCommand(org.bukkit.plugin.Plugin plugin, ItemManager itemManager) {
        this.itemManager = itemManager;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;
        Inventory inv = Bukkit.createInventory(null, 9, ChatColor.RED + "Mua Lồng Kính");
        inv.setItem(4, itemManager.createShopItem());
        p.openInventory(inv);
        return true;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().equals(ChatColor.RED + "Mua Lồng Kính")) return;
        e.setCancelled(true);
        if (e.getCurrentItem() == null || e.getCurrentItem().getType() != Material.GHAST_TEAR) return;

        Player p = (Player) e.getWhoClicked();

        int beaconCount = 0;
        for (ItemStack it : p.getInventory().getContents())
            if (it != null && it.getType() == Material.BEACON) beaconCount += it.getAmount();

        if (beaconCount < 5) {
            p.sendMessage(ChatColor.RED + "Bạn cần 5 Beacon để mua!");
            return;
        }

        int remaining = 5;
        for (ItemStack it : p.getInventory().getContents()) {
            if (it == null || it.getType() != Material.BEACON) continue;
            int amt = it.getAmount();
            if (amt > remaining) { it.setAmount(amt - remaining); remaining = 0; break; }
            else { it.setAmount(0); remaining -= amt; if (remaining <= 0) break; }
        }

        p.getInventory().addItem(itemManager.createSkillItem());
        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
        p.sendMessage(ChatColor.GREEN + "Bạn đã mua thành công Ngọc Lồng Kính!");
        p.closeInventory();
    }
}
