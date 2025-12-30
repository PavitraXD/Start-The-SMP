package com.smpstart;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.Sound;
import org.bukkit.GameMode;
import org.bukkit.Difficulty;

public class ConfigManager {
    
    private final SMPStartPlugin plugin;
    private FileConfiguration config;
    
    public ConfigManager(SMPStartPlugin plugin) {
        this.plugin = plugin;
        reloadConfiguration();
    }
    
    public void reloadConfiguration() {
        this.config = plugin.getConfig();
    }
    
    // Countdown settings
    public int getCountdownDuration() {
        return config.getInt("countdown.duration", 10);
    }
    
    public boolean useActionBar() {
        return config.getBoolean("countdown.use-actionbar", false);
    }
    
    // World border settings
    public double getInitialBorderSize() {
        return config.getDouble("world-border.initial-size", 50.0);
    }
    
    public double getFinalBorderSize() {
        return config.getDouble("world-border.final-size", 5000.0);
    }
    
    public long getExpansionDuration() {
        return config.getLong("world-border.expansion-duration", 300);
    }
    
    public String getWorldName() {
        return config.getString("world-border.world-name", "");
    }
    
    // Title settings
    public String getStartTitle() {
        return config.getString("titles.start-title.title", "§c§lSMP IS STARTING");
    }
    
    public String getStartSubtitle() {
        return config.getString("titles.start-title.subtitle", "§7Get ready for the adventure!");
    }
    
    public int getStartTitleFadeIn() {
        return config.getInt("titles.start-title.fade-in", 10);
    }
    
    public int getStartTitleStay() {
        return config.getInt("titles.start-title.stay", 40);
    }
    
    public int getStartTitleFadeOut() {
        return config.getInt("titles.start-title.fade-out", 10);
    }
    
    public String getCountdownTitle() {
        return config.getString("titles.countdown-title.title", "§e§l{time}");
    }
    
    public String getCountdownSubtitle() {
        return config.getString("titles.countdown-title.subtitle", "§7seconds remaining...");
    }
    
    public int getCountdownTitleFadeIn() {
        return config.getInt("titles.countdown-title.fade-in", 0);
    }
    
    public int getCountdownTitleStay() {
        return config.getInt("titles.countdown-title.stay", 25);
    }
    
    public int getCountdownTitleFadeOut() {
        return config.getInt("titles.countdown-title.fade-out", 5);
    }
    
    public String getStartedTitle() {
        return config.getString("titles.started-title.title", "§a§lSMP STARTED");
    }
    
    public String getStartedSubtitle() {
        return config.getString("titles.started-title.subtitle", "§7Good luck, have fun!");
    }
    
    public int getStartedTitleFadeIn() {
        return config.getInt("titles.started-title.fade-in", 10);
    }
    
    public int getStartedTitleStay() {
        return config.getInt("titles.started-title.stay", 60);
    }
    
    public int getStartedTitleFadeOut() {
        return config.getInt("titles.started-title.fade-out", 20);
    }
    
    // Action bar settings
    public String getActionBarMessage() {
        return config.getString("actionbar.countdown-message", "§e§lCountdown: §f{time} §7seconds remaining");
    }
    
    // Sound settings
    public boolean isSoundEnabled() {
        return config.getBoolean("sounds.enabled", true);
    }
    
    public org.bukkit.Sound getSound() {
        try {
            return org.bukkit.Sound.valueOf(config.getString("sounds.sound", "ENTITY_WITHER_SPAWN"));
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid sound in config: " + config.getString("sounds.sound"));
            return org.bukkit.Sound.ENTITY_WITHER_SPAWN;
        }
    }
    
    public float getVolume() {
        return (float) config.getDouble("sounds.volume", 1.0);
    }
    
    public float getPitch() {
        return (float) config.getDouble("sounds.pitch", 1.0);
    }
    
    // Plugin settings
    public boolean isSingleUse() {
        return config.getBoolean("plugin.single-use", true);
    }
    
    public boolean isDebugLogging() {
        return config.getBoolean("plugin.debug-logging", false);
    }
    
    // Messages
    public String getMessage(String path) {
        return config.getString("messages." + path, "Message not found: " + path);
    }

    // Phase settings
    public double getStartingBorderSize() {
        return config.getDouble("phases.starting.border-size", 50.0);
    }

    public Difficulty getStartingDifficulty() {
        try {
            return Difficulty.valueOf(config.getString("phases.starting.difficulty", "PEACEFUL").toUpperCase());
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid starting difficulty in config: " + config.getString("phases.starting.difficulty"));
            return Difficulty.PEACEFUL;
        }
    }

    public GameMode getStartingGamemode() {
        String gamemodeStr = config.getString("phases.starting.gamemode", "SURVIVAL");
        if (gamemodeStr.equalsIgnoreCase("NONE")) return null;
        try {
            return GameMode.valueOf(gamemodeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid starting gamemode in config: " + gamemodeStr);
            return GameMode.SURVIVAL;
        }
    }

    public boolean isStartingBlockBreakingAllowed() {
        return config.getBoolean("phases.starting.allow-block-breaking", false);
    }

    public boolean isStartingMobSpawningAllowed() {
        return config.getBoolean("phases.starting.allow-mob-spawning", false);
    }

    public double getStartedBorderSize() {
        return config.getDouble("phases.started.border-size", 5000.0);
    }

    public Difficulty getStartedDifficulty() {
        try {
            return Difficulty.valueOf(config.getString("phases.started.difficulty", "NORMAL").toUpperCase());
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid started difficulty in config: " + config.getString("phases.started.difficulty"));
            return Difficulty.NORMAL;
        }
    }

    public GameMode getStartedGamemode() {
        String gamemodeStr = config.getString("phases.started.gamemode", "SURVIVAL");
        if (gamemodeStr.equalsIgnoreCase("NONE")) return null;
        try {
            return GameMode.valueOf(gamemodeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid started gamemode in config: " + gamemodeStr);
            return GameMode.SURVIVAL;
        }
    }

    // PvP settings
    public boolean isPvpCountdownEnabled() {
        return config.getBoolean("pvp.countdown-enabled", true);
    }

    public int getPvpCountdownDuration() {
        return config.getInt("pvp.countdown-duration", 30);
    }

    // Effects settings
    public boolean isFireworksEnabled() {
        return config.getBoolean("effects.fireworks.enabled", true);
    }

    public int getFireworkCount() {
        return config.getInt("effects.fireworks.count", 10);
    }

    public boolean isRainbowParticlesEnabled() {
        return config.getBoolean("effects.rainbow-particles.enabled", true);
    }

    public boolean isBorderParticlesEnabled() {
        return config.getBoolean("effects.border-particles.enabled", true);
    }

    public boolean isMultiLayeredSoundEnabled() {
        return config.getBoolean("effects.multi-layered-sound.enabled", true);
    }
}
