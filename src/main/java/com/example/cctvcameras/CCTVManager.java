package com.example.cctvcameras;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CCTVManager {
    public final CCTVCamerasPlugin plugin;
    private final Map<String, Location> activeCameras = new HashMap<>();
    private final Map<UUID, CCTVSession> activeSessions = new HashMap<>();
    private final Map<UUID, Location> pendingReturns = new HashMap<>();

    public CCTVManager(CCTVCamerasPlugin plugin) {
        this.plugin = plugin;
    }

    public void addCamera(Location loc) {
        activeCameras.put(getLocationKey(loc), loc);
    }

    public void removeCamera(Location loc) {
        activeCameras.remove(getLocationKey(loc));
    }

    public Map<String, Location> getActiveCameras() {
        return activeCameras;
    }

    public void addSession(Player player, CCTVSession session) {
        activeSessions.put(player.getUniqueId(), session);
    }

    public CCTVSession getSession(Player player) {
        return activeSessions.get(player.getUniqueId());
    }

    public void removeSession(Player player) {
        activeSessions.remove(player.getUniqueId());
    }

    public Map<UUID, CCTVSession> getActiveSessions() {
        return activeSessions;
    }

    public void addPendingReturn(UUID uuid, Location loc) {
        pendingReturns.put(uuid, loc);
    }

    public boolean hasPendingReturn(UUID uuid) {
        return pendingReturns.containsKey(uuid);
    }

    public Location getPendingReturn(UUID uuid) {
        return pendingReturns.get(uuid);
    }

    public void clearPendingReturn(UUID uuid) {
        pendingReturns.remove(uuid);
    }

    private String getLocationKey(Location loc) {
        return loc.getWorld().getName() + "_" + loc.getBlockX() + "_" + loc.getBlockY() + "_" + loc.getBlockZ();
    }

    public void saveCameras() {
        List<String> cameraList = new ArrayList<>();
        for (Location loc : activeCameras.values()) {
            cameraList.add(loc.getWorld().getName() + ":" + loc.getX() + ":" + loc.getY() + ":" + loc.getZ());
        }
        FileConfiguration config = plugin.getConfig();
        config.set("cameras", cameraList);
        plugin.saveConfig();
    }

    public void loadCameras() {
        FileConfiguration config = plugin.getConfig();
        List<String> cameraList = config.getStringList("cameras");
        for (String s : cameraList) {
            String[] parts = s.split(":");
            if (parts.length == 4) {
                String worldName = parts[0];
                try {
                    double x = Double.parseDouble(parts[1]);
                    double y = Double.parseDouble(parts[2]);
                    double z = Double.parseDouble(parts[3]);
                    World world = plugin.getServer().getWorld(worldName);
                    if (world != null) {
                        Location loc = new Location(world, x, y, z);
                        activeCameras.put(getLocationKey(loc), loc);
                    }
                } catch (NumberFormatException e) {
                    plugin.getLogger().warning("Invalid camera location: " + s);
                }
            }
        }
    }

    public void cleanup() {
        for (CCTVSession session : activeSessions.values()) {
            session.endSession();
            if (session.getPlayer().isOnline()) {
                session.getPlayer().setGameMode(session.getPreviousGameMode());
                session.getPlayer().teleport(session.getOriginalLocation());
            }
            session.getCameraVillager().remove();
        }
        activeSessions.clear();
    }
}
