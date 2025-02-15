package com.example.cctvcameras;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class CCTVVillagerDamageListener implements Listener {

    private final CCTVManager manager;

    public CCTVVillagerDamageListener(CCTVManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onVillagerDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Villager) {
            Villager villager = (Villager) entity;
            for (CCTVSession session : manager.getActiveSessions().values()) {
                if (session.getCameraVillager().equals(villager)) {
                    if (session.getPlayer().isOnline()) {
                        Player player = session.getPlayer();
                        player.sendMessage(ChatColor.RED + "CCTV session ended because the camera villager was damaged!");
                        player.teleport(session.getOriginalLocation());
                        player.setGameMode(session.getPreviousGameMode());
                    }
                    villager.remove();
                    session.endSession();
                    manager.removeSession(session.getPlayer());
                    break;
                }
            }
        }
    }
}
