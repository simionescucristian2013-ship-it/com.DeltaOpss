package com.DeltaOpss.listener;

import com.DeltaOpss.manager.HeartManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class HungerListener implements Listener {

    private final HeartManager heartManager;

    public HungerListener(HeartManager heartManager) {
        this.heartManager = heartManager;
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        var data = heartManager.getData(player.getUniqueId());
        if (!data.isHeartless() && !data.isImmortal()) return;

        event.setCancelled(true);
        player.setFoodLevel(20);
        player.setSaturation(20.0f);
        player.setExhaustion(0.0f);
    }
}
