package com.smpstart;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.World;

public class SMPStartCommand implements CommandExecutor {

    private final SMPStartPlugin plugin;
    private final SMPManager smpManager;

    public SMPStartCommand(SMPStartPlugin plugin, SMPManager smpManager) {
        this.plugin = plugin;
        this.smpManager = smpManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check permission
        if (!sender.hasPermission("smpstart.admin")) {
            sender.sendMessage(plugin.getConfigManager().getMessage("player.no-permission"));
            return true;
        }

        if (args.length == 0) {
            // Default behavior: start SMP
            return handleStart(sender);
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "center":
                return handleCenter(sender);
            case "starting":
                return handleStarting(sender, args);
            case "start":
                return handleStart(sender, args);
            case "stop":
            case "reset":
                return handleStopReset(sender);
            case "reload":
                return handleReload(sender);
            case "help":
                return handleHelp(sender);
            default:
                sender.sendMessage("§cUsage: /smpstart [center|starting|start|stop|reset|reload|help]");
                return true;
        }
    }

    private boolean handleStart(CommandSender sender) {
        return handleStart(sender, new String[0]);
    }

    private boolean handleStart(CommandSender sender, String[] args) {
        // Check if SMP already started (single-use check)
        if (plugin.getConfigManager().isSingleUse() && smpManager.hasStarted()) {
            sender.sendMessage(plugin.getConfigManager().getMessage("player.smp-already-started"));
            plugin.getLogger().info(plugin.getConfigManager().getMessage("console.already-started"));
            return true;
        }

        // Handle border size override: /smpstart start border <size>
        if (args.length >= 3 && args[1].equalsIgnoreCase("border")) {
            try {
                double borderSize = Double.parseDouble(args[2]);
                // TODO: Implement border size override for started phase
                sender.sendMessage("§aBorder size override not yet implemented");
            } catch (NumberFormatException e) {
                sender.sendMessage("§cInvalid border size: " + args[2]);
                return true;
            }
        }

        // Start the SMP
        String playerName = sender instanceof Player ? sender.getName() : "Console";
        sender.sendMessage(plugin.getConfigManager().getMessage("player.smp-starting"));

        smpManager.startSMP(playerName);

        return true;
    }

    private boolean handleCenter(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;
        Location loc = player.getLocation();
        World world = getTargetWorld();

        if (world != null) {
            world.getWorldBorder().setCenter(loc);
            sender.sendMessage("§aWorld border center set to your location!");
            plugin.getLogger().info("World border center set by " + player.getName() + " to " + loc.getBlockX() + ", " + loc.getBlockZ());
        } else {
            sender.sendMessage("§cFailed to set world border center!");
        }

        return true;
    }

    private boolean handleStarting(CommandSender sender, String[] args) {
        // Check if SMP already started
        if (smpManager.hasStarted()) {
            sender.sendMessage("§cCannot switch to starting phase - SMP has already started!");
            return true;
        }

        // Handle border size: /smpstart starting border <size>
        if (args.length >= 3 && args[1].equalsIgnoreCase("border")) {
            try {
                double borderSize = Double.parseDouble(args[2]);
                // TODO: Implement border size setting for starting phase
                sender.sendMessage("§aStarting border size override not yet implemented");
            } catch (NumberFormatException e) {
                sender.sendMessage("§cInvalid border size: " + args[2]);
                return true;
            }
        } else {
            // Switch to starting phase
            smpManager.setPhase(SMPPhase.STARTING);
            sender.sendMessage("§aSwitched to Starting phase!");
        }

        return true;
    }

    private World getTargetWorld() {
        String worldName = plugin.getConfigManager().getWorldName();
        World world;

        if (worldName.isEmpty()) {
            world = plugin.getServer().getWorlds().get(0); // Main world
        } else {
            world = plugin.getServer().getWorld(worldName);
            if (world == null) {
                plugin.getLogger().warning("World '" + worldName + "' not found, using main world");
                world = plugin.getServer().getWorlds().get(0);
            }
        }

        return world;
    }

    private boolean handleStopReset(CommandSender sender) {
        if (!smpManager.hasStarted()) {
            sender.sendMessage("§cThe SMP has not been started yet - nothing to reset!");
            return true;
        }

        // Attempt to reset the SMP
        if (smpManager.resetSMP()) {
            sender.sendMessage("§aSMP has been successfully reset!");
            plugin.getLogger().info("SMP reset initiated by " + (sender instanceof Player ? sender.getName() : "Console"));
        } else {
            sender.sendMessage("§cFailed to reset SMP - it may not have been started.");
        }

        return true;
    }

    private boolean handleReload(CommandSender sender) {
        try {
            plugin.reloadPluginConfig();
            sender.sendMessage("§aStart The SMP configuration reloaded successfully!");
            plugin.getLogger().info("Configuration reloaded by " + (sender instanceof Player ? sender.getName() : "Console"));
        } catch (Exception e) {
            sender.sendMessage("§cFailed to reload configuration: " + e.getMessage());
            plugin.getLogger().warning("Configuration reload failed: " + e.getMessage());
        }
        return true;
    }

    private boolean handleHelp(CommandSender sender) {
        sender.sendMessage("§6§l=== Start The SMP Commands ===");
        sender.sendMessage("§e/smpstart §7- Start the SMP with epic effects");
        sender.sendMessage("§e/smpstart center §7- Set world border center to your location");
        sender.sendMessage("§e/smpstart starting §7- Switch to Starting phase (peaceful)");
        sender.sendMessage("§e/smpstart starting border <size> §7- Set Starting phase border size");
        sender.sendMessage("§e/smpstart start border <size> §7- Set Started phase border size and start SMP");
        sender.sendMessage("§e/smpstart stop §7- Stop and reset the SMP to initial state");
        sender.sendMessage("§e/smpstart reset §7- Same as stop - reset SMP completely");
        sender.sendMessage("§e/smpstart reload §7- Reload configuration without restart");
        sender.sendMessage("§e/smpstart help §7- Show this help message");
        sender.sendMessage("§7§oAll commands require smpstart.admin permission");
        return true;
    }
}
