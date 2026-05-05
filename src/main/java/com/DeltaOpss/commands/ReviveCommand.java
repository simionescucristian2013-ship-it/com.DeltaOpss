package com.DeltaOpss.commands;

import com.DeltaOpss.HeartlessPlugin;
import com.DeltaOpss.PlayerData;
import com.DeltaOpss.manager.HeartManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReviveCommand implements CommandExecutor {

    private final HeartlessPlugin plugin;
    private final HeartManager heartManager;

    public ReviveCommand(HeartlessPlugin plugin, HeartManager heartManager) {
        this.plugin = plugin;
        this.heartManager = heartManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("heartless.revive")) {
            sender.sendMessage("§cNo permission!");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage("§cUsage: /revive <player>");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        PlayerData data = heartManager.getData(target.getUniqueId());
        data.setHearts(plugin.getConfig().getInt("revive-hearts", 10));
        data.setHeartless(false);
        data.setImmortal(false);
        data.setLastWarning(false);
        heartManager.saveData();

        sender.sendMessage("§aRevived " + target.getName() + " with " + data.getHearts() + " hearts.");
        if (target.isOnline()) {
            heartManager.applyToPlayer(target.getPlayer());
        }
        return true;
    }
}
