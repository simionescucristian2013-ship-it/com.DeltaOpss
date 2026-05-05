package com.DeltaOpss.commands;

import com.DeltaOpss.HeartlessPlugin;
import com.DeltaOpss.manager.HeartManager;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.*;

public class OwnerCommand implements CommandExecutor, TabCompleter {

    private final HeartManager heartManager;

    public OwnerCommand(HeartManager heartManager) {
        this.heartManager = heartManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("heartless.owner")) {
            sender.sendMessage("§cNo permission!");
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage("§cUsage: /owner <set|unset> <player>");
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

        heartManager.getData(target.getUniqueId()).setImmortal(set);
        heartManager.applyToPlayer(target);
        sender.sendMessage("§7" + target.getName() + " is now " + (set ? "§fIMMORTAL" : "§aNormal") + "§7.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        // Same as HeartlessCommand
        List<String> list = new ArrayList<>();
        if (args.length == 1) { list.add("set"); list.add("unset"); }
        else if (args.length == 2) {
            String prefix = args[1].toLowerCase();
            Bukkit.getOnlinePlayers().forEach(p -> {
                if (p.getName().toLowerCase().startsWith(prefix)) list.add(p.getName());
            });
        }
        return list;
    }
}
