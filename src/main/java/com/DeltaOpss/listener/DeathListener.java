package com.DeltaOpss.listener;

import com.DeltaOpss.manager.HeartManager;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    private final HeartManager heartManager;

    public DeathListener(HeartManager heartManager) {
        this.heartManager = heartManager;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player deadPlayer = e.getEntity();
        deadPlayer.getWorld().getNearbyPlayers(deadPlayer.getLocation(), 100.0)
                .forEach(player -> player.playSound(deadPlayer.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1.0f, 1.0f));

        heartManager.handleDeath(e.getEntity());
    }
}
