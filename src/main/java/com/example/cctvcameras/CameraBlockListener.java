package com.example.cctvcameras;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

public class CameraBlockListener implements Listener {

    private final CCTVManager manager;

    public CameraBlockListener(CCTVManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getItemInHand() != null && event.getItemInHand().getType() == org.bukkit.Material.PLAYER_HEAD) {
            if (event.getItemInHand().hasItemMeta()) {
                SkullMeta meta = (SkullMeta) event.getItemInHand().getItemMeta();
                NamespacedKey key = new NamespacedKey(manager.plugin, CCTVCommandExecutor.CAMERA_KEY);
                if (meta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                    manager.addCamera(event.getBlock().getLocation());
                    event.getPlayer().sendMessage(ChatColor.GREEN + "CCTV Camera placed!");
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (manager.getActiveCameras().containsValue(block.getLocation())) {
            manager.removeCamera(block.getLocation());
            event.getPlayer().sendMessage(ChatColor.RED + "CCTV Camera broken!");
        }
    }
}
