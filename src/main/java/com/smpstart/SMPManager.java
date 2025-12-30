package com.smpstart;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.entity.Firework;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.Particle;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.bukkit.GameMode;
import org.bukkit.Difficulty;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.sound.Sound;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class SMPManager implements Listener {

    private final SMPStartPlugin plugin;
    private final ConfigManager config;
    private final AtomicBoolean hasStarted = new AtomicBoolean(false);
    private SMPPhase currentPhase = SMPPhase.STARTING;
    private BukkitTask countdownTask;
    private BukkitTask particleTask;
    private BukkitTask pvpCountdownTask;
    private boolean pvpEnabled = false;
    private final Random random = new Random();
    
    public SMPManager(SMPStartPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfigManager();
    }
    
    public boolean hasStarted() {
        return hasStarted.get();
    }
    
    public void startSMP(String initiatorName) {
        if (hasStarted.getAndSet(true)) {
            return; // Already started
        }
        
        plugin.getLogger().info(config.getMessage("console.smp-started").replace("{player}", initiatorName));
        
        // Setup world border immediately
        setupInitialWorldBorder();
        
        // Show starting title to all players
        showStartingTitle();
        
        // Start countdown
        startCountdown();
    }
    
    private void setupInitialWorldBorder() {
        World world = getTargetWorld();
        if (world == null) return;
        
        WorldBorder border = world.getWorldBorder();
        border.setCenter(world.getSpawnLocation());
        border.setSize(config.getInitialBorderSize());
        
        if (config.isDebugLogging()) {
            plugin.getLogger().info("World border set to initial size: " + config.getInitialBorderSize());
        }
    }
    
    private void showStartingTitle() {
        Component titleComponent = Component.text(config.getStartTitle());
        Component subtitleComponent = Component.text(config.getStartSubtitle());
        
        Title title = Title.title(
            titleComponent,
            subtitleComponent,
            Title.Times.times(
                Duration.ofMillis(config.getStartTitleFadeIn() * 50L),
                Duration.ofMillis(config.getStartTitleStay() * 50L),
                Duration.ofMillis(config.getStartTitleFadeOut() * 50L)
            )
        );
        
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.showTitle(title);
            player.playSound(player.getLocation(), org.bukkit.Sound.BLOCK_NOTE_BLOCK_PLING, 0.7f, 1.5f);
        }
    }
    
    private void startCountdown() {
        AtomicInteger timeLeft = new AtomicInteger(config.getCountdownDuration());
        
        countdownTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            int current = timeLeft.get();
            
            if (current <= 0) {
                // Countdown finished
                countdownTask.cancel();
                onCountdownFinished();
                return;
            }
            
            // Show countdown to players
            showCountdown(current);
            timeLeft.decrementAndGet();
            
        }, 20L, 20L); // Run every second (20 ticks)
    }
    
    private void showCountdown(int timeLeft) {
        if (config.useActionBar()) {
            // Show in action bar
            Component actionBarMessage = Component.text(
                config.getActionBarMessage().replace("{time}", String.valueOf(timeLeft))
            );

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendActionBar(actionBarMessage);
                player.playSound(player.getLocation(), org.bukkit.Sound.BLOCK_NOTE_BLOCK_HAT, 0.5f, 2.0f);

                // Show rainbow particles around player
                if (config.isRainbowParticlesEnabled()) {
                    showRainbowParticles(player);
                }
            }
        } else {
            // Show as title
            Component titleComponent = Component.text(
                config.getCountdownTitle().replace("{time}", String.valueOf(timeLeft))
            );
            Component subtitleComponent = Component.text(config.getCountdownSubtitle());

            Title title = Title.title(
                titleComponent,
                subtitleComponent,
                Title.Times.times(
                    Duration.ofMillis(config.getCountdownTitleFadeIn() * 50L),
                    Duration.ofMillis(config.getCountdownTitleStay() * 50L),
                    Duration.ofMillis(config.getCountdownTitleFadeOut() * 50L)
                )
            );

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.showTitle(title);
                player.playSound(player.getLocation(), org.bukkit.Sound.BLOCK_NOTE_BLOCK_HAT, 0.5f, 2.0f);

                // Show rainbow particles around player
                if (config.isRainbowParticlesEnabled()) {
                    showRainbowParticles(player);
                }
            }
        }
    }
    
    private void onCountdownFinished() {
        // Switch to STARTED phase
        setPhase(SMPPhase.STARTED);

        // Expand world border first
        expandWorldBorder();

        // Show "SMP Started" title
        showStartedTitle();

        // Launch epic fireworks
        if (config.isFireworksEnabled()) {
            launchFireworks();
        }

        // Play multi-layered sound effects
        if (config.isSoundEnabled()) {
            playMultiLayeredSound();
        }

        plugin.getLogger().info("SMP countdown completed, switched to STARTED phase");
    }
    
    private void showStartedTitle() {
        Component titleComponent = Component.text(config.getStartedTitle());
        Component subtitleComponent = Component.text(config.getStartedSubtitle());
        
        Title title = Title.title(
            titleComponent,
            subtitleComponent,
            Title.Times.times(
                Duration.ofMillis(config.getStartedTitleFadeIn() * 50L),
                Duration.ofMillis(config.getStartedTitleStay() * 50L),
                Duration.ofMillis(config.getStartedTitleFadeOut() * 50L)
            )
        );
        
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.showTitle(title);
            player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        }
    }
    
    private void playStartSound() {
        try {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.8f);
                player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.5f, 1.2f);
                plugin.getLogger().info("Sound played for: " + player.getName());
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Sound error: " + e.getMessage());
        }
    }

    // Multi-layered sound effects for SMP start
    private void playMultiLayeredSound() {
        try {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Location loc = player.getLocation();

                // Layer 1: Epic orchestral hits
                player.playSound(loc, org.bukkit.Sound.ITEM_GOAT_HORN_SOUND_0, 1.0f, 0.5f);

                // Layer 2: Thunder and lightning effects
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    player.playSound(loc, org.bukkit.Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 0.7f, 0.8f);
                }, 5L);

                // Layer 3: Celebration sounds
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    player.playSound(loc, org.bukkit.Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
                    player.playSound(loc, org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 0.8f, 1.2f);
                }, 15L);

                // Layer 4: Final celebration burst
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    player.playSound(loc, org.bukkit.Sound.ENTITY_FIREWORK_ROCKET_BLAST_FAR, 0.6f, 1.5f);
                    player.playSound(loc, org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, 2.0f);
                }, 30L);

                plugin.getLogger().info("Multi-layered sound played for: " + player.getName());
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Multi-layered sound error: " + e.getMessage());
        }
    }
    
    private void expandWorldBorder() {
        World world = getTargetWorld();
        if (world == null) {
            plugin.getLogger().warning("Target world is null, cannot expand border");
            return;
        }
        
        WorldBorder border = world.getWorldBorder();
        double currentSize = border.getSize();
        double finalSize = config.getFinalBorderSize();
        long duration = config.getExpansionDuration();
        
        plugin.getLogger().info("Current border size: " + currentSize);
        plugin.getLogger().info("Expanding to: " + finalSize + " over " + duration + " seconds");
        
        // Set border expansion immediately on main thread
        border.setSize(finalSize, duration);
        plugin.getLogger().info("World border expansion command executed successfully");
    }
    
    private World getTargetWorld() {
        String worldName = config.getWorldName();
        World world;

        if (worldName.isEmpty()) {
            world = Bukkit.getWorlds().get(0); // Main world
        } else {
            world = Bukkit.getWorld(worldName);
            if (world == null) {
                plugin.getLogger().warning("World '" + worldName + "' not found, using main world");
                world = Bukkit.getWorlds().get(0);
            }
        }

        return world;
    }

    // Phase management methods
    public SMPPhase getCurrentPhase() {
        return currentPhase;
    }

    public void setPhase(SMPPhase phase) {
        if (this.currentPhase == phase) return;

        this.currentPhase = phase;
        applyPhaseSettings();

        plugin.getLogger().info("Switched to " + phase.name() + " phase");

        // Notify players
        String message = phase == SMPPhase.STARTING ?
            config.getMessage("player.phase-starting") :
            config.getMessage("player.phase-started");
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(message);
        }
    }

    private void applyPhaseSettings() {
        World world = getTargetWorld();
        if (world == null) return;

        if (currentPhase == SMPPhase.STARTING) {
            // Starting phase settings
            world.setDifficulty(config.getStartingDifficulty());
            world.setPVP(false);
            pvpEnabled = false;

            // Set world border to starting size
            WorldBorder border = world.getWorldBorder();
            border.setSize(config.getStartingBorderSize());

            // Apply gamemode if configured
            GameMode startingGamemode = config.getStartingGamemode();
            if (startingGamemode != null) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.setGameMode(startingGamemode);
                }
            }

        } else if (currentPhase == SMPPhase.STARTED) {
            // Started phase settings
            world.setDifficulty(config.getStartedDifficulty());

            // Set world border to started size
            WorldBorder border = world.getWorldBorder();
            border.setSize(config.getStartedBorderSize());

            // Apply gamemode if configured
            GameMode startedGamemode = config.getStartedGamemode();
            if (startedGamemode != null) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.setGameMode(startedGamemode);
                }
            }

            // Start PvP countdown if enabled
            if (config.isPvpCountdownEnabled()) {
                startPvpCountdown();
            } else {
                enablePvp();
            }
        }

        // Start/stop world border particles based on phase
        if (currentPhase == SMPPhase.STARTED && config.isBorderParticlesEnabled()) {
            startBorderParticles();
        } else {
            stopBorderParticles();
        }
    }

    private void startPvpCountdown() {
        AtomicInteger timeLeft = new AtomicInteger(config.getPvpCountdownDuration());

        pvpCountdownTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            int current = timeLeft.get();

            if (current <= 0) {
                pvpCountdownTask.cancel();
                enablePvp();
                return;
            }

            // Show PvP countdown
            String message = config.getMessage("player.pvp-countdown").replace("{time}", String.valueOf(current));
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendActionBar(Component.text(message));
                player.playSound(player.getLocation(), org.bukkit.Sound.BLOCK_NOTE_BLOCK_HAT, 0.3f, 1.0f);
            }

            timeLeft.decrementAndGet();
        }, 20L, 20L);
    }

    private void enablePvp() {
        World world = getTargetWorld();
        if (world != null) {
            world.setPVP(true);
            pvpEnabled = true;

            // Notify players
            String message = config.getMessage("player.pvp-enabled");
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(message);
                player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            }
        }
    }

    public boolean isPvpEnabled() {
        return pvpEnabled;
    }

    // Border particles
    private void startBorderParticles() {
        particleTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            World world = getTargetWorld();
            if (world == null) return;

            WorldBorder border = world.getWorldBorder();
            Location center = border.getCenter();
            double size = border.getSize() / 2.0;

            // Create particle ring around border
            for (int i = 0; i < 360; i += 10) {
                double angle = Math.toRadians(i);
                double x = center.getX() + (size * Math.cos(angle));
                double z = center.getZ() + (size * Math.sin(angle));
                Location particleLoc = new Location(world, x, center.getY() + 1, z);

                world.spawnParticle(Particle.DUST, particleLoc, 1, new Particle.DustOptions(Color.RED, 1.0f));
            }
        }, 20L, 40L); // Every 2 seconds
    }

    private void stopBorderParticles() {
        if (particleTask != null && !particleTask.isCancelled()) {
            particleTask.cancel();
            particleTask = null;
        }
    }

    // Rainbow particles during countdown
    private void showRainbowParticles(Player player) {
        Location loc = player.getLocation();
        World world = loc.getWorld();
        if (world == null) return;

        for (int i = 0; i < 20; i++) {
            double angle = (i / 20.0) * 2 * Math.PI;
            double radius = 1.5;
            double x = loc.getX() + radius * Math.cos(angle);
            double z = loc.getZ() + radius * Math.sin(angle);
            Location particleLoc = new Location(world, x, loc.getY() + 1, z);

            Color color = Color.fromRGB(
                random.nextInt(256),
                random.nextInt(256),
                random.nextInt(256)
            );

            world.spawnParticle(Particle.DUST, particleLoc, 1, new Particle.DustOptions(color, 1.0f));
        }
    }

    // Epic fireworks
    private void launchFireworks() {
        World world = getTargetWorld();
        if (world == null) return;

        Location spawnLoc = world.getSpawnLocation();

        for (int i = 0; i < config.getFireworkCount(); i++) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                Location fireworkLoc = spawnLoc.clone().add(
                    (random.nextDouble() - 0.5) * 20,
                    random.nextDouble() * 10 + 10,
                    (random.nextDouble() - 0.5) * 20
                );

                Firework firework = world.spawn(fireworkLoc, Firework.class);
                FireworkMeta meta = firework.getFireworkMeta();

                // Random firework effects
                FireworkEffect.Type[] types = {
                    FireworkEffect.Type.BALL,
                    FireworkEffect.Type.BALL_LARGE,
                    FireworkEffect.Type.STAR,
                    FireworkEffect.Type.CREEPER,
                    FireworkEffect.Type.BURST
                };

                FireworkEffect.Builder effectBuilder = FireworkEffect.builder()
                    .with(types[random.nextInt(types.length)])
                    .withColor(getRandomColor())
                    .withFade(getRandomColor());

                if (random.nextBoolean()) {
                    effectBuilder.withTrail();
                }
                if (random.nextBoolean()) {
                    effectBuilder.withFlicker();
                }

                FireworkEffect effect = effectBuilder.build();

                meta.addEffect(effect);
                meta.setPower(random.nextInt(3) + 1);
                firework.setFireworkMeta(meta);
            }, i * 10L); // Stagger fireworks
        }
    }

    private Color getRandomColor() {
        Color[] colors = {
            Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW,
            Color.PURPLE, Color.ORANGE, Color.fromRGB(255, 192, 203), Color.AQUA // Using RGB for pink
        };
        return colors[random.nextInt(colors.length)];
    }

    // Event handlers
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (currentPhase == SMPPhase.STARTING && !config.isStartingBlockBreakingAllowed()) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(config.getMessage("player.block-break-denied"));
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (currentPhase == SMPPhase.STARTING && !config.isStartingMobSpawningAllowed()) {
            if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL) {
                event.setCancelled(true);
            }
        }
    }
    
    public void cleanup() {
        if (countdownTask != null && !countdownTask.isCancelled()) {
            countdownTask.cancel();
        }
        if (particleTask != null && !particleTask.isCancelled()) {
            particleTask.cancel();
        }
        if (pvpCountdownTask != null && !pvpCountdownTask.isCancelled()) {
            pvpCountdownTask.cancel();
        }
    }

    // Reset/stop SMP functionality
    public boolean resetSMP() {
        if (!hasStarted.get()) {
            return false; // SMP hasn't been started, nothing to reset
        }

        // Cancel all ongoing tasks
        cleanup();

        // Reset all flags and states
        hasStarted.set(false);
        currentPhase = SMPPhase.STARTING;
        pvpEnabled = false;

        // Reset world settings to initial state
        World world = getTargetWorld();
        if (world != null) {
            // Reset to initial border size
            world.getWorldBorder().setSize(config.getInitialBorderSize());

            // Reset difficulty to starting phase difficulty
            world.setDifficulty(config.getStartingDifficulty());

            // Reset PvP to disabled
            world.setPVP(false);

            // Reset gamemode if configured
            GameMode startingGamemode = config.getStartingGamemode();
            if (startingGamemode != null) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.setGameMode(startingGamemode);
                }
            }
        }

        plugin.getLogger().info("SMP has been reset to initial state");

        // Notify all players
        String message = config.getMessage("player.smp-reset");
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("smpstart.admin")) {
                player.sendMessage(message);
            }
        }

        return true;
    }
}
