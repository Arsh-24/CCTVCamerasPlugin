package com.example.cctvcameras;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

public class CCTVCommandExecutor implements CommandExecutor {

    private final CCTVCamerasPlugin plugin;
    private final CCTVManager manager;

    // Keys to mark our custom items
    public static final String CAMERA_KEY = "cctv_camera";
    public static final String MONITOR_KEY = "cctv_monitor";

    public CCTVCommandExecutor(CCTVCamerasPlugin plugin, CCTVManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Expected usage:
        // /cctv camera give <player>
        // /cctv monitor give <player>
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: /cctv <camera|monitor> give <player>");
            return true;
        }

        String type = args[0];
        String action = args[1];
        String targetName = args[2];

        OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);
        if (!(target instanceof Player) || !target.isOnline()) {
            sender.sendMessage(ChatColor.RED + "Player not found or offline!");
            return true;
        }
        Player targetPlayer = (Player) target;

        if (action.equalsIgnoreCase("give")) {
            if (type.equalsIgnoreCase("camera")) {
                // Create a custom player head item as the CCTV camera
                ItemStack cameraItem = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta meta = (SkullMeta) cameraItem.getItemMeta();
                meta.setDisplayName(ChatColor.GOLD + "CCTV Camera");
                // Mark the item with our custom key using NamespacedKey
                NamespacedKey key = new NamespacedKey(plugin, CAMERA_KEY);
                meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "true");
                cameraItem.setItemMeta(meta);

                targetPlayer.getInventory().addItem(cameraItem);
                sender.sendMessage(ChatColor.GREEN + "Gave CCTV Camera to " + targetPlayer.getName());
            } else if (type.equalsIgnoreCase("monitor")) {
                // Create a custom monitor item (using a map as an example)
                ItemStack monitorItem = new ItemStack(Material.MAP);
                ItemMeta monitorMeta = monitorItem.getItemMeta();
                monitorMeta.setDisplayName(ChatColor.AQUA + "CCTV Monitor");
                NamespacedKey key = new NamespacedKey(plugin, MONITOR_KEY);
                monitorMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "true");
                monitorItem.setItemMeta(monitorMeta);

                targetPlayer.getInventory().addItem(monitorItem);
                sender.sendMessage(ChatColor.GREEN + "Gave CCTV Monitor to " + targetPlayer.getName());
            } else {
                sender.sendMessage(ChatColor.RED + "Unknown type: " + type);
            }
        }
        return true;
    }
}
