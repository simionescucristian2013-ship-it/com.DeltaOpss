package com.DeltaOpss;

import com.DeltaOpss.commands.*;
import com.DeltaOpss.listener.*;
import com.DeltaOpss.manager.HeartManager;
import org.bukkit.plugin.java.JavaPlugin;

public class HeartlessPlugin extends JavaPlugin {

    private HeartManager heartManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        heartManager = new HeartManager(this);

        getServer().getPluginManager().registerEvents(new JoinListener(this, heartManager), this);
        getServer().getPluginManager().registerEvents(new DeathListener(heartManager), this);
        getServer().getPluginManager().registerEvents(new HeartDamageListener(heartManager), this);
        getServer().getPluginManager().registerEvents(new HungerListener(heartManager), this);
        getServer().getPluginManager().registerEvents(new RegenListener(heartManager), this);
        getServer().getPluginManager().registerEvents(new ChatListener(heartManager), this);

        HeartlessCommand heartlessCommand = new HeartlessCommand(heartManager);
        OwnerCommand ownerCommand = new OwnerCommand(heartManager);
        HeartsCommand heartsCommand = new HeartsCommand(heartManager);

        getCommand("heartless").setExecutor(heartlessCommand);
        getCommand("heartless").setTabCompleter(heartlessCommand);
        getCommand("owner").setExecutor(ownerCommand);
        getCommand("owner").setTabCompleter(ownerCommand);
        getCommand("hearts").setExecutor(heartsCommand);
        getCommand("hearts").setTabCompleter(heartsCommand);
        getCommand("revive").setExecutor(new ReviveCommand(this, heartManager));

        getLogger().info("HeartlessPlugin v1.1.0 enabled!");
    }

    @Override
    public void onDisable() {
        if (heartManager != null) {
            heartManager.saveData();
        }
        getLogger().info("HeartlessPlugin disabled.");
    }

    public HeartManager getHeartManager() {
        return heartManager;
    }
}
