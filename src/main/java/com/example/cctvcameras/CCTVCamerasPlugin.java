package com.example.cctvcameras;

import org.bukkit.plugin.java.JavaPlugin;

public class CCTVCamerasPlugin extends JavaPlugin {
    public CCTVManager cctvManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        cctvManager = new CCTVManager(this);
        cctvManager.loadCameras();
        getCommand("cctv").setExecutor(new CCTVCommandExecutor(this, cctvManager));
        getServer().getPluginManager().registerEvents(new CameraBlockListener(cctvManager), this);
        getServer().getPluginManager().registerEvents(new MonitorListener(cctvManager), this);
        getServer().getPluginManager().registerEvents(new CCTVModeListener(cctvManager), this);
        getServer().getPluginManager().registerEvents(new CCTVGUIListener(cctvManager), this);
        getServer().getPluginManager().registerEvents(new CCTVQuitListener(cctvManager), this);
        getServer().getPluginManager().registerEvents(new CCTVVillagerDamageListener(cctvManager), this);
        getServer().getPluginManager().registerEvents(new CCTVJoinListener(cctvManager), this);
        getLogger().info("CCTVCamerasPlugin enabled!");
    }

    @Override
    public void onDisable() {
        cctvManager.cleanup();
        cctvManager.saveCameras();
        getLogger().info("CCTVCamerasPlugin disabled.");
    }
}
