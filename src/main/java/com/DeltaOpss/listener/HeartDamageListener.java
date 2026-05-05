package com.DeltaOpss.listener;

import com.DeltaOpss.manager.HeartManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class HeartDamageListener implements Listener {

    private final HeartManager heartManager;

    public HeartDamageListener(HeartManager heartManager) {
        this.heartManager = heartManager;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player p)) return;

        var data = heartManager.getData(p.getUniqueId());
        if (data.isImmortal()) {
            e.setCancelled(true);
            return;
        }
        if (data.isHeartless()) {
            if (e.getCause() == EntityDamageEvent.DamageCause.WITHER) {
                e.setCancelled(true);
                p.setHealth(20.0);
                return;
            }
            e.setDamage(0.0);
            p.sendHurtAnimation(0.0f);
            p.setHealth(20.0);
        }
    }
}
