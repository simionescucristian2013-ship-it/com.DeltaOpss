package com.DeltaOpss.commands;

import com.DeltaOpss.HeartlessPlugin;
import com.DeltaOpss.manager.HeartManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HeartlessCommand implements CommandExecutor, TabCompleter {

    private final HeartManager heartManager;

    public HeartlessCommand(HeartManager heartManager) {
        this.heartManager = heartManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("heartless.heartless")) {
            sender.sendMessage("§cNo permission!");
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage("§cUsage: /heartless <set|unset> <player>");
            return true;
        }

        boolean set = args[0].equalsIgnoreCase("set");
        if (!set && !args[0].equalsIgnoreCase("unset")) {
            sender.sendMessage("§cUse §eset §cor §eunset");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[1]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found!");
            return true;
        }

        heartManager.getData(target.getUniqueId()).setHeartless(set);
        heartManager.applyToPlayer(target);
        sender.sendMessage("§7" + target.getName() + " is now " + (set ? "§8HEARTLESS" : "§aNormal") + "§7.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            list.add("set");
            list.add("unset");
        } else if (args.length == 2) {
            String prefix = args[1].toLowerCase();
            Bukkit.getOnlinePlayers().forEach(p -> {
                if (p.getName().toLowerCase().startsWith(prefix)) list.add(p.getName());
            });
        }
        return list;
    }
}
