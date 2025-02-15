package com.example.cctvcameras;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class CCTVJoinListener implements Listener {
    private final CCTVManager manager;

    public CCTVJoinListener(CCTVManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (manager.hasPendingReturn(player.getUniqueId())) {
            player.teleport(manager.getPendingReturn(player.getUniqueId()));
            player.setGameMode(GameMode.SURVIVAL);
            player.sendMessage(ChatColor.GREEN + "Your CCTV session was terminated while you were offline. You have been returned to your original location.");
            manager.clearPendingReturn(player.getUniqueId());
        }
    }
}
