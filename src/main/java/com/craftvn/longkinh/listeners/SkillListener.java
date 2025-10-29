package com.craftvn.longkinh.listeners;

import com.craftvn.longkinh.utils.CageManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class SkillListener implements Listener {

    private final CageManager cageManager;

    public SkillListener(CageManager cageManager) {
        this.cageManager = cageManager;
    }

    @EventHandler
    public void onUse(PlayerInteractEvent e) {
        Action a = e.getAction();
        if (a != Action.RIGHT_CLICK_AIR && a != Action.RIGHT_CLICK_BLOCK) return;

        Player p = e.getPlayer();
        EquipmentSlot hand = e.getHand();
        if (hand == null) return;

        ItemStack used = (hand == EquipmentSlot.HAND)
                ? p.getInventory().getItemInMainHand()
                : p.getInventory().getItemInOffHand();

        if (used == null || used.getType() != Material.GHAST_TEAR || used.getItemMeta() == null || used.getItemMeta().getLore() == null)
            return;

        // Kích hoạt
        e.setCancelled(true);
        cageManager.spawnCage(p);
        p.sendMessage(ChatColor.RED + "Bạn đã kích hoạt kỹ năng Lồng Kính!");

        // Tiêu hao 1 ngọc đúng theo tay
        used.setAmount(used.getAmount() - 1);
        if (used.getAmount() <= 0) {
            if (hand == EquipmentSlot.HAND) {
                p.getInventory().setItemInMainHand(null);
            } else {
                p.getInventory().setItemInOffHand(null);
            }
        } else {
            if (hand == EquipmentSlot.HAND) {
                p.getInventory().setItemInMainHand(used);
            } else {
                p.getInventory().setItemInOffHand(used);
            }
        }
    }
}
