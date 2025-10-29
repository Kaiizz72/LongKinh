package com.craftvn.longkinh.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CageManager {

    private final Plugin plugin;

    // ref-count bảo vệ khối kính: key = world:x:y:z
    private final Map<String, Integer> glassRef = new ConcurrentHashMap<>();

    public CageManager(Plugin plugin) {
        this.plugin = plugin;
    }

    private String keyOf(Block b) {
        Location l = b.getLocation();
        return l.getWorld().getName() + ":" + l.getBlockX() + ":" + l.getBlockY() + ":" + l.getBlockZ();
    }

    public boolean isProtected(Block b) {
        return glassRef.getOrDefault(keyOf(b), 0) > 0 && b.getType() == Material.RED_STAINED_GLASS;
    }

    public void spawnCage(Player owner) {
        Location center = owner.getLocation().clone().add(0, 1, 0);
        World world = center.getWorld();
        if (world == null) return;

        int radius = 7;
        List<Block> blocks = new ArrayList<>();
        List<String> keys = new ArrayList<>();

        for (int x = -radius; x <= radius; x++) {
            for (int y = 0; y <= radius; y++) { // mũ úp: chỉ nửa trên
                for (int z = -radius; z <= radius; z++) {
                    double dist = Math.sqrt(x * x + y * y + z * z);
                    if (dist <= radius && dist >= radius - 1) {
                        Block b = world.getBlockAt(center.getBlockX() + x, center.getBlockY() + y, center.getBlockZ() + z);
                        String key = keyOf(b);

                        glassRef.merge(key, 1, Integer::sum);
                        if (b.getType() == Material.AIR) {
                            b.setType(Material.RED_STAINED_GLASS, false);
                        }
                        blocks.add(b);
                        keys.add(key);
                    }
                }
            }
        }

        // Gỡ sau 15s
        Bukkit.getScheduler().runTaskLater(plugin, () -> removeCage(blocks, keys), 15 * 20L);

        // Theo dõi số người trong lồng, nếu <=1 thì 5s sau gỡ (nếu vẫn <=1)
        Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                if (!center.isWorldLoaded()) return;
                long count = center.getWorld().getPlayers().stream()
                        .filter(p -> p.getLocation().distance(center) <= radius)
                        .count();
                if (count <= 1) {
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        long c2 = center.getWorld().getPlayers().stream()
                                .filter(p -> p.getLocation().distance(center) <= radius)
                                .count();
                        if (c2 <= 1) {
                            removeCage(blocks, keys);
                        }
                    }, 5 * 20L);
                }
            }
        }, 40L, 40L); // kiểm tra mỗi 2s
    }

    private void removeCage(List<Block> blocks, List<String> keys) {
        for (String key : keys) {
            glassRef.computeIfPresent(key, (k, v) -> v - 1);
        }
        for (int i = 0; i < keys.size(); i++) {
            Block b = blocks.get(i);
            String key = keys.get(i);
            Integer ref = glassRef.get(key);
            if (ref == null || ref <= 0) {
                glassRef.remove(key);
                if (b.getType() == Material.RED_STAINED_GLASS) {
                    b.setType(Material.AIR, false);
                }
            }
        }
        blocks.clear();
        keys.clear();
    }
}
