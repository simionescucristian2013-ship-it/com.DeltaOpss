package com.DeltaOpss.manager;

import com.DeltaOpss.HeartlessPlugin;
import com.DeltaOpss.PlayerData;
import com.DeltaOpss.util.NametagUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HeartManager {

    private final HeartlessPlugin plugin;
    private final Map<UUID, PlayerData> dataMap = new HashMap<>();
    private final File dataFile;
    private FileConfiguration dataConfig;

    public HeartManager(HeartlessPlugin plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "data.yml");
        loadData();
    }

    public PlayerData getData(UUID uuid) {
        return dataMap.computeIfAbsent(uuid, k -> new PlayerData());
    }

    public void applyToPlayer(Player player) {
        PlayerData data = getData(player.getUniqueId());
        double maxHealth = data.isHeartless() || data.isImmortal()
                ? 20.0
                : Math.max(2.0, data.getHearts() * 2.0);
        AttributeInstance maxHealthAttribute = player.getAttribute(Attribute.MAX_HEALTH);

        if (maxHealthAttribute != null) {
            maxHealthAttribute.setBaseValue(maxHealth);
        }
        if (player.getHealth() > maxHealth) {
            player.setHealth(maxHealth);
        }

        if (data.isHeartless()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, Integer.MAX_VALUE, 0, false, false, false));
        } else {
            player.removePotionEffect(PotionEffectType.WITHER);
        }

        if (data.isHeartless() || data.isImmortal()) {
            player.setHealth(maxHealth);
            player.setFoodLevel(20);
            player.setSaturation(20.0f);
            player.setExhaustion(0.0f);
        }

        if (data.isImmortal()) {
            player.setAllowFlight(true);
        } else if (player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR) {
            player.setFlying(false);
            player.setAllowFlight(false);
        }

        NametagUtil.updateNametag(player, data);
        saveData();
    }

    public void handleDeath(Player player) {
        PlayerData data = getData(player.getUniqueId());

        if (!data.isNormal()) return;

        int lost = plugin.getConfig().getInt("hearts-lost-per-death", 2);
        data.setHearts(data.getHearts() - lost);
        saveData();

        if (data.getHearts() <= 0) {
            if (plugin.getConfig().getBoolean("enable-elimination", true)) {
                int banHours = plugin.getConfig().getInt("elimination-ban-hours", 4);
                Instant unbanAt = Instant.now().plus(Duration.ofHours(banHours));
                String unbanTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm z")
                        .withZone(ZoneId.systemDefault())
                        .format(unbanAt);
                String message = plugin.getConfig().getString("elimination-message",
                        "You lost all your hearts and have been eliminated.")
                        + "\nTemporary ban: " + banHours + " hours."
                        + "\nYou will be unbanned at: " + unbanTime;

                Bukkit.broadcastMessage("§c" + player.getName() + " has run out of hearts and he got banned!");
                player.ban(message, unbanAt, "HeartlessPlugin", true);
            } else {
                data.setHearts(1);
                saveData();
            }
        }

        Bukkit.getScheduler().runTask(plugin, () -> applyToPlayer(player));
    }

    public void setHearts(Player player, int amount) {
        getData(player.getUniqueId()).setHearts(amount);
        saveData();
        applyToPlayer(player);
    }

    public void addHearts(Player player, int amount) {
        getData(player.getUniqueId()).addHearts(amount);
        saveData();
        applyToPlayer(player);
    }

    public void loadData() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        ConfigurationSection players = dataConfig.getConfigurationSection("players");
        if (players == null) return;

        for (String key : players.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(key);
                PlayerData data = new PlayerData();
                data.setHearts(players.getInt(key + ".hearts", plugin.getConfig().getInt("starting-hearts", 10)));
                data.setHeartless(players.getBoolean(key + ".heartless", false));
                data.setImmortal(players.getBoolean(key + ".immortal", false));
                data.setLast(players.getBoolean(key + ".last", false));
                dataMap.put(uuid, data);
            } catch (IllegalArgumentException ignored) {
                plugin.getLogger().warning("Skipping invalid player UUID in data.yml: " + key);
            }
        }
    }

    public void saveData() {
        if (dataConfig == null) {
            dataConfig = new YamlConfiguration();
        }

        dataConfig.set("players", null);
        for (Map.Entry<UUID, PlayerData> entry : dataMap.entrySet()) {
            String path = "players." + entry.getKey();
            PlayerData data = entry.getValue();
            dataConfig.set(path + ".hearts", data.getHearts());
            dataConfig.set(path + ".heartless", data.isHeartless());
            dataConfig.set(path + ".immortal", data.isImmortal());
            dataConfig.set(path + ".last", data.isLast());
        }

        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save data.yml: " + e.getMessage());
        }
    }
}
