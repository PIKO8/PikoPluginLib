package ru.piko.pikopluginlib;

import org.bukkit.Bukkit;
import ru.piko.pikopluginlib.PlayersData.APlayerData;
import ru.piko.pikopluginlib.PlayersData.PlayerData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Main extends PikoPlugin {

    private static Main plugin;

    private final Map<String, PikoPlugin> pikoPluginMap = new HashMap<>();
    /**
     * A map that stores player data for each player by their UUID.
     */
    private final Map<UUID, PlayerData> playerDataMap = new HashMap<>();


    @Override
    public String getPluginId() {
        return "lib";
    }

    @Override
    public void onStart() {}

    @Override
    public void onStop() {}

    @Override
    public void onEnable() {
        plugin = this;
        this.pluginId = getPluginId();
        addPikoPlugin(pluginId, this);
    }

    public static Main getPlugin() {
        return plugin;
    }

    public void addPikoPlugin(String pluginId, PikoPlugin pikoPlugin) {
        if (!pikoPluginMap.containsKey(pluginId)) {
            pikoPluginMap.put(pluginId, pikoPlugin);
        } else {
            // Логирование или обработка ситуации, когда плагин уже зарегистрирован
            System.out.println("Плагин с идентификатором " + pluginId + " уже зарегистрирован.");
        }
    }

    public boolean hasPikoPlugin(String id) {
        return pikoPluginMap.containsKey(id);
    }

    public PikoPlugin getPikoPlugin(String id) {
        return pikoPluginMap.get(id);
    }

    public void removePikoPlugin(String id) {
        pikoPluginMap.remove(id);
    }

    // PLAYER DATA
    public PlayerData getPlayerData(UUID owner) {
        if (playerDataMap.containsKey(owner)) {
            return playerDataMap.get(owner);
        }
        PlayerData data = new PlayerData(Bukkit.getPlayer(owner));
        playerDataMap.put(owner, data);
        return data;
    }

    public void removePlayerData(UUID owner) {
        playerDataMap.remove(owner);
    }

}
