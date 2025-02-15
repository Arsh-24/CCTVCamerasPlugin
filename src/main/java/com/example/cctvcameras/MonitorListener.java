package com.example.cctvcameras;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

public class MonitorListener implements Listener {
    private final CCTVManager manager;

    public MonitorListener(CCTVManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItem() != null && event.getItem().getType() == Material.MAP) {
            if (event.getItem().hasItemMeta()) {
                NamespacedKey key = new NamespacedKey(manager.plugin, CCTVCommandExecutor.MONITOR_KEY);
                if (event.getItem().getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                    event.setCancelled(true);
                    openCameraGUI(event.getPlayer());
                }
            }
        }
    }

    private void openCameraGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 9, ChatColor.DARK_AQUA + "CCTV Cameras");
        int slot = 0;
        for (Map.Entry<String, org.bukkit.Location> entry : manager.getActiveCameras().entrySet()) {
            org.bukkit.Location loc = entry.getValue();
            ItemStack cameraIcon = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) cameraIcon.getItemMeta();
            meta.setDisplayName(ChatColor.GOLD + "Camera at "
                    + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
            cameraIcon.setItemMeta(meta);
            gui.setItem(slot, cameraIcon);
            slot++;
            if (slot >= gui.getSize()) break;
        }
        player.openInventory(gui);
    }
}
