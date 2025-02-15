package com.example.cctvcameras;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class CCTVGUIListener implements Listener {

    private final CCTVManager manager;

    public CCTVGUIListener(CCTVManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(ChatColor.DARK_AQUA + "CCTV Cameras")) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            ItemStack clicked = event.getCurrentItem();
            if (clicked == null) return;
            String display = clicked.getItemMeta().getDisplayName();
            try {
                String coords = display.replace(ChatColor.GOLD.toString(), "")
                        .replace("Camera at ", "");
                String[] parts = coords.split(", ");
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                int z = Integer.parseInt(parts[2]);
                Location cameraLoc = player.getWorld().getBlockAt(x, y, z).getLocation();
                player.closeInventory();

                Villager villager = player.getWorld().spawn(player.getLocation(), Villager.class, v -> {
                    v.setCustomName(ChatColor.LIGHT_PURPLE + player.getName() + " is watching CCTV");
                    v.setCustomNameVisible(true);
                });

                GameMode prevMode = player.getGameMode();
                Location originalLocation = player.getLocation();

                player.setGameMode(GameMode.SPECTATOR);
                player.teleport(cameraLoc);

                CCTVSession session = new CCTVSession(player, originalLocation, cameraLoc, prevMode, villager);
                manager.addSession(player, session);

                BukkitTask task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!player.isOnline()) {
                            cancel();
                            return;
                        }
                        if (player.getLocation().distance(cameraLoc) > 10) {
                            player.teleport(cameraLoc);
                            player.sendMessage(ChatColor.RED + "You cannot move too far from the CCTV camera!");
                        }
                    }
                }.runTaskTimer(manager.plugin, 20, 20);
                // Store the task so we can cancel it on exit
                session.setDistanceCheckTask(task);

                player.sendMessage(ChatColor.GREEN + "Entered CCTV mode. Sneak 3 times in 10 seconds to exit.");
            } catch (Exception e) {
                player.sendMessage(ChatColor.RED + "Error parsing camera location.");
            }
        }
    }
}
