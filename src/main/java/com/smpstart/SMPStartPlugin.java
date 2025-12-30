package com.smpstart;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.logging.Logger;

public class SMPStartPlugin extends JavaPlugin {
    
    private static SMPStartPlugin instance;
    private SMPManager smpManager;
    private ConfigManager configManager;
    private Logger logger;
    
    @Override
    public void onEnable() {
        instance = this;
        logger = getLogger();
        
        // Initialize configuration
        saveDefaultConfig();
        configManager = new ConfigManager(this);
        
        // Initialize SMP manager
        smpManager = new SMPManager(this);

        // Register event listener
        getServer().getPluginManager().registerEvents(smpManager, this);

        // Register commands
        SMPStartCommand startCommand = new SMPStartCommand(this, smpManager);
        getCommand("smpstart").setExecutor(startCommand);
        
        logger.info("Start The SMP plugin enabled successfully!");
    }
    
    @Override
    public void onDisable() {
        if (smpManager != null) {
            smpManager.cleanup();
        }
        logger.info("Start The SMP plugin disabled.");
    }
    
    public static SMPStartPlugin getInstance() {
        return instance;
    }
    
    public SMPManager getSMPManager() {
        return smpManager;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    public void reloadPluginConfig() {
        reloadConfig();
        configManager.reloadConfiguration();
        logger.info(configManager.getMessage("console.config-reloaded"));
    }
}
