package com.example.cctvcameras;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.scheduler.BukkitTask;

public class CCTVSession {
    private final Player player;
    private final Location originalLocation;
    private final Location cameraLocation;
    private final GameMode previousGameMode;
    private final Villager cameraVillager;
    private int sneakCount = 0;
    private long firstSneakTime = 0;
    private BukkitTask distanceCheckTask;

    public CCTVSession(Player player, Location originalLocation, Location cameraLocation,
                       GameMode previousGameMode, Villager cameraVillager) {
        this.player = player;
        this.originalLocation = originalLocation;
        this.cameraLocation = cameraLocation;
        this.previousGameMode = previousGameMode;
        this.cameraVillager = cameraVillager;
    }

    public Player getPlayer() {
        return player;
    }

    public Location getOriginalLocation() {
        return originalLocation;
    }

    public Location getCameraLocation() {
        return cameraLocation;
    }

    public GameMode getPreviousGameMode() {
        return previousGameMode;
    }

    public Villager getCameraVillager() {
        return cameraVillager;
    }

    public int getSneakCount() {
        return sneakCount;
    }

    public void incrementSneakCount() {
        long now = System.currentTimeMillis();
        if (firstSneakTime == 0 || now - firstSneakTime > 10000) {
            firstSneakTime = now;
            sneakCount = 1;
        } else {
            sneakCount++;
        }
    }

    public void resetSneakCount() {
        sneakCount = 0;
        firstSneakTime = 0;
    }

    public void setDistanceCheckTask(BukkitTask task) {
        this.distanceCheckTask = task;
    }

    public void cancelDistanceTask() {
        if (distanceCheckTask != null) {
            distanceCheckTask.cancel();
        }
    }

    public void endSession() {
        cancelDistanceTask();
    }
}
