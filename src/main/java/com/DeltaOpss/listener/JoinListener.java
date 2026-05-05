package com.DeltaOpss.listener;

import com.DeltaOpss.HeartlessPlugin;
import com.DeltaOpss.PlayerData;
import com.DeltaOpss.manager.HeartManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final HeartlessPlugin plugin;
    private final HeartManager heartManager;

    public JoinListener(HeartlessPlugin plugin, HeartManager heartManager) {
        this.plugin = plugin;
        this.heartManager = heartManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            PlayerData data = heartManager.getData(e.getPlayer().getUniqueId());
            if (data.getHearts() <= 0) {
                data.setHearts(plugin.getConfig().getInt("revive-hearts", 10));
            }
            heartManager.applyToPlayer(e.getPlayer());
        }, 10L);
    }
}
