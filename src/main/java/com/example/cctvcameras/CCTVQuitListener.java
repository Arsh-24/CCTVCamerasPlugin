package com.example.cctvcameras;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class CCTVQuitListener implements Listener {
    private final CCTVManager manager;

    public CCTVQuitListener(CCTVManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        CCTVSession session = manager.getSession(player);
        if (session != null) {
            manager.addPendingReturn(player.getUniqueId(), session.getOriginalLocation());
            session.endSession();
            session.getCameraVillager().remove();
            manager.removeSession(player);
        }
    }
}
