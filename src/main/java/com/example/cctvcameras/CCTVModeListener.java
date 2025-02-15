package com.example.cctvcameras;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class CCTVModeListener implements Listener {

    private final CCTVManager manager;

    public CCTVModeListener(CCTVManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        CCTVSession session = manager.getSession(player);
        if (session != null) {
            if (event.isSneaking()) {
                session.incrementSneakCount();
                if (session.getSneakCount() >= 3) {
                    session.endSession();
                    player.teleport(session.getOriginalLocation());
                    player.setGameMode(session.getPreviousGameMode());
                    session.getCameraVillager().remove();
                    manager.removeSession(player);
                    player.sendMessage(ChatColor.GREEN + "Exited CCTV mode.");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (manager.getSession(event.getPlayer()) != null) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "You cannot run commands while in CCTV mode.");
        }
    }
}
