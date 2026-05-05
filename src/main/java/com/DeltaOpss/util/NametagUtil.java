package com.DeltaOpss.util;

import com.DeltaOpss.PlayerData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class NametagUtil {

    public static final String HEART = "\u2665";
    public static final String WARNING = "\u26A0";

    public static void updateNametag(Player player, PlayerData data) {
        if (!Bukkit.getPluginManager().getPlugin("HeartlessPlugin")
                .getConfig().getBoolean("nametag-enabled", true)) return;

        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        String teamName = "HH_" + player.getUniqueId().toString().substring(0, 12);

        Team team = board.getTeam(teamName);
        if (team == null) team = board.registerNewTeam(teamName);

        team.prefix(heartComponent(data).append(Component.space()));
        team.suffix(Component.space().append(heartComponent(data)));

        team.getEntries().forEach(team::removeEntry);
        team.addEntry(player.getName());
    }

    public static Component heartComponent(PlayerData data) {
        return Component.text(getHeartSymbol(data), getHeartColor(data))
                .decoration(TextDecoration.BOLD, false);
    }

    public static String getHeartSymbol(PlayerData data) {
        if (data.getHearts() > 20) return WARNING;
        return HEART;
    }

    public static TextColor getHeartColor(PlayerData data) {
        if (data.isImmortal()) return TextColor.color(0xFFFFFF);
        if (data.isHeartless()) return TextColor.color(0x222222);
        if (data.getHearts() > 20) return TextColor.color(0xAA00FF);
        if (data.getHearts() > 10) return TextColor.color(0xAA00FF);
        if (data.getHearts() >= 10) return TextColor.color(0x00FF00);
        if (data.getHearts() >= 6) return TextColor.color(0xFFFF00);
        return TextColor.color(0xFF4444);
    }

    public static void removeNametag(Player player) {
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        String teamName = "HH_" + player.getUniqueId().toString().substring(0, 12);
        Team team = board.getTeam(teamName);
        if (team != null) team.removeEntry(player.getName());
    }
}
