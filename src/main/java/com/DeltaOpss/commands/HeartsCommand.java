package com.DeltaOpss.commands;

import com.DeltaOpss.PlayerData;
import com.DeltaOpss.manager.HeartManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HeartsCommand implements CommandExecutor, TabCompleter {

    private final HeartManager heartManager;
    private final List<String> allowedHearts = Arrays.asList("2", "4", "6", "8", "10", "12", "14", "16", "18", "20", "22", "24", "26", "28", "30");

    public HeartsCommand(HeartManager heartManager) {
        this.heartManager = heartManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("heartless.hearts")) {
            sender.sendMessage("§cNo permission!");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("§cUsage: §e/hearts <set|add|remove> <player> <2/4/6/8/10/12/14/16/18/20/22/24/26/28/30>");
            sender.sendMessage("§eOr: §e/hearts <player> §7(to check current hearts)");
            return true;
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                sender.sendMessage("§cPlayer not found!");
                return true;
            }
            PlayerData data = heartManager.getData(target.getUniqueId());
            sender.sendMessage("§7" + target.getName() + " currently has §c" + data.getHearts() + " §7hearts.");
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage("§cUsage: §e/hearts <set|add|remove> <player> <amount>");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[1]);
        if (target == null) {
            sender.sendMessage("§cPlayer not online!");
            return true;
        }

        String amountStr = args[2];
        if (!allowedHearts.contains(amountStr)) {
            sender.sendMessage("§cInvalid amount! Allowed: §e2,4,6,8,10,12,14,16,18,20,22,24,26,28,30");
            return true;
        }

        int amount = Integer.parseInt(amountStr);
        PlayerData data = heartManager.getData(target.getUniqueId());
        String action = args[0].toLowerCase();

        switch (action) {
            case "set":
                heartManager.setHearts(target, amount);
                sender.sendMessage("§aSet " + target.getName() + "'s hearts to §c" + amount);
                break;

            case "add":
                heartManager.addHearts(target, amount);
                sender.sendMessage("§aAdded §c" + amount + " §ahearts to " + target.getName());
                break;

            case "remove":
            case "subtract":
            case "sub":
                heartManager.setHearts(target, Math.max(0, data.getHearts() - amount));
                sender.sendMessage("§aRemoved §c" + amount + " §ahearts from " + target.getName());
                break;

            default:
                sender.sendMessage("§cInvalid action! Use: set, add, or remove");
                return true;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.addAll(Arrays.asList("set", "add", "remove"));
        } else if (args.length == 2) {
            String prefix = args[1].toLowerCase();
            Bukkit.getOnlinePlayers().forEach(p -> {
                if (p.getName().toLowerCase().startsWith(prefix)) {
                    completions.add(p.getName());
                }
            });
        } else if (args.length == 3) {
            completions.addAll(allowedHearts);
        }

        return completions;
    }
}
