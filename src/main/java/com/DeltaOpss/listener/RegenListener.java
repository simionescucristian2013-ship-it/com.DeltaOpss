package com.DeltaOpss.listener;

import com.DeltaOpss.manager.HeartManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class RegenListener implements Listener {

    private final HeartManager heartManager;

    public RegenListener(HeartManager heartManager) {
        this.heartManager = heartManager;
    }

    @EventHandler
    public void onRegen(EntityRegainHealthEvent e) {
        if (!(e.getEntity() instanceof Player p)) return;
        if (heartManager.getData(p.getUniqueId()).isHeartless()) {
            e.setCancelled(true);
        }
    }
}