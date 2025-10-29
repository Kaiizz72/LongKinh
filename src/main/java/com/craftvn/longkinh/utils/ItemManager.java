package com.craftvn.longkinh.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class ItemManager {

    private String color(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    // Item chỉ để HIỂN THỊ trong GUI, không bao giờ rơi vào túi người chơi
    public ItemStack createShopItem() {
        ItemStack tear = new ItemStack(Material.GHAST_TEAR);
        ItemMeta meta = tear.getItemMeta();
        if (meta == null) return tear;

        meta.setDisplayName(color("&k❖&r &x&F&F&3&3&3&3Kỹ năng Lồng Kính&r &k❖"));
        List<String> lore = Arrays.asList(
            color("&7➛ &x&F&F&A&A&0&0Giá: 5 Beacon"),
            color("&7➛ &x&F&F&F&F&0&0Click để mua")
        );
        meta.setLore(lore);
        meta.addItemFlags(
            ItemFlag.HIDE_ATTRIBUTES,
            ItemFlag.HIDE_ENCHANTS,
            ItemFlag.HIDE_UNBREAKABLE,
            ItemFlag.HIDE_POTION_EFFECTS
        );
        tear.setItemMeta(meta);
        return tear;
    }

    // Item người chơi nhận để sử dụng
    public ItemStack createSkillItem() {
        ItemStack tear = new ItemStack(Material.GHAST_TEAR);
        ItemMeta meta = tear.getItemMeta();
        if (meta == null) return tear;

        meta.setDisplayName(color("&k❖&r &x&F&F&3&3&3&3Ngọc Lồng Kính&r &k❖"));
        List<String> lore = Arrays.asList(
            color("&7➛ &x&5&5&F&F&5&5Chuột phải khi ở tay chính hoặc offhand"),
            color("&7➛ &x&D&D&2&2&F&FTạo Lồng Kính đỏ tồn tại 15s"),
            color("&7➛ &x&F&F&3&3&3&3Nếu còn 1 người → biến mất sau 5s")
        );
        meta.setLore(lore);
        meta.addItemFlags(
            ItemFlag.HIDE_ATTRIBUTES,
            ItemFlag.HIDE_ENCHANTS,
            ItemFlag.HIDE_UNBREAKABLE,
            ItemFlag.HIDE_POTION_EFFECTS
        );
        tear.setItemMeta(meta);
        return tear;
    }
}
