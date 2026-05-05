package com.DeltaOpss.listener;

import com.DeltaOpss.PlayerData;
import com.DeltaOpss.manager.HeartManager;
import com.DeltaOpss.util.NametagUtil;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {

    private final HeartManager heartManager;

    public ChatListener(HeartManager heartManager) {
        this.heartManager = heartManager;
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        PlayerData data = heartManager.getData(event.getPlayer().getUniqueId());
        Component heart = NametagUtil.heartComponent(data);

        event.renderer((source, sourceDisplayName, message, viewer) ->
                heart.append(Component.space())
                        .append(Component.text(source.getName(), NamedTextColor.WHITE))
                        .append(Component.space())
                        .append(heart)
                        .append(Component.text(": ", NamedTextColor.WHITE))
                        .append(message.colorIfAbsent(NamedTextColor.WHITE)));
    }
}
