package com.smpstart;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Material;
import net.kyori.adventure.text.Component;
import java.util.Arrays;

public class SMPGUICommand implements CommandExecutor {

    private final SMPStartPlugin plugin;
    private final SMPManager smpManager;

    public SMPGUICommand(SMPStartPlugin plugin, SMPManager smpManager) {
        this.plugin = plugin;
        this.smpManager = smpManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used by players!");
            return true;
        }

        Player player = (Player) sender;

        // Check permission
        if (!player.hasPermission("smpstart.admin")) {
            player.sendMessage(plugin.getConfigManager().getMessage("player.no-permission"));
            return true;
        }

        openGUI(player);
        return true;
    }

    private void openGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, Component.text("§6§lStart The SMP - Control Panel"));

        // Current phase display
        ItemStack phaseItem = createItem(Material.BEACON,
            "§bCurrent Phase: §f" + smpManager.getCurrentPhase().name(),
            "§7Click to switch phases"
        );
        gui.setItem(4, phaseItem);

        // Start SMP button
        if (!smpManager.hasStarted()) {
            ItemStack startItem = createItem(Material.EMERALD_BLOCK,
                "§a§lSTART SMP",
                "§7Begins the epic SMP launch sequence",
                "§7with fireworks, particles, and countdown!"
            );
            gui.setItem(10, startItem);
        } else {
            ItemStack startedItem = createItem(Material.REDSTONE_BLOCK,
                "§c§lSMP ALREADY STARTED",
                "§7The SMP launch sequence has been completed"
            );
            gui.setItem(10, startedItem);
        }

        // Reset SMP button
        if (smpManager.hasStarted()) {
            ItemStack resetItem = createItem(Material.BARRIER,
                "§c§lRESET SMP",
                "§7Stops and resets the SMP to initial state",
                "§7Cancels all effects and returns to starting phase"
            );
            gui.setItem(12, resetItem);
        } else {
            ItemStack resetItem = createItem(Material.GRAY_DYE,
                "§7§lRESET SMP",
                "§7No SMP to reset - hasn't been started yet"
            );
            gui.setItem(12, resetItem);
        }

        // Phase control buttons
        ItemStack startingPhaseItem = createItem(Material.GRASS_BLOCK,
            "§aSwitch to Starting Phase",
            "§7Peaceful environment for preparation",
            "§7- Small world border",
            "§7- No PvP",
            "§7- Block breaking disabled"
        );
        gui.setItem(13, startingPhaseItem);

        ItemStack startedPhaseItem = createItem(Material.NETHERRACK,
            "§cSwitch to Started Phase",
            "§7Full SMP gameplay mode",
            "§7- Large world border",
            "§7- PvP enabled",
            "§7- Normal difficulty"
        );
        gui.setItem(15, startedPhaseItem);

        // Information items
        ItemStack infoItem = createItem(Material.BOOK,
            "§e§lSMP Information",
            "§7Phase: §f" + smpManager.getCurrentPhase().name(),
            "§7Started: §f" + (smpManager.hasStarted() ? "Yes" : "No"),
            "§7PvP Enabled: §f" + (smpManager.isPvpEnabled() ? "Yes" : "No")
        );
        gui.setItem(22, infoItem);

        // Border control
        ItemStack borderItem = createItem(Material.SHIELD,
            "§d§lWorld Border Control",
            "§7Current size: §f" + getCurrentBorderSize(),
            "§7Click to set center to your location"
        );
        gui.setItem(20, borderItem);

        player.openInventory(gui);
    }

    private ItemStack createItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(name));
        meta.lore(Arrays.asList(lore).stream().map(Component::text).toList());
        item.setItemMeta(meta);
        return item;
    }

    private String getCurrentBorderSize() {
        try {
            double size = plugin.getServer().getWorlds().get(0).getWorldBorder().getSize();
            return String.format("%.0f", size);
        } catch (Exception e) {
            return "Unknown";
        }
    }
}
