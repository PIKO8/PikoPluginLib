package ru.piko.pikopluginlib;

import org.bukkit.plugin.java.JavaPlugin;
import ru.piko.pikopluginlib.Listeners.MenuEvent;
import ru.piko.pikopluginlib.PlayersData.PlayerData;

import java.util.*;

public final class Main extends JavaPlugin {

    private static Main plugin;

    private final Map<String, PikoPlugin> pikoPluginMap = new HashMap<>();
    /**
     * A map that stores player data for each player by their UUID.
     */
    private final Map<UUID, PlayerData> playerDataMap = new HashMap<>();

    @Override
    public void onEnable() {
        plugin = this;
        System.out.println("PikoPluginLib загружен!");
        getServer().getPluginManager().registerEvents(new MenuEvent(), this);
    }

    public static Main getPlugin() {
        return plugin;
    }

    public void addPikoPlugin(String pluginId, PikoPlugin pikoPlugin) {
        if (!pikoPluginMap.containsKey(pluginId)) {
            pikoPluginMap.put(pluginId, pikoPlugin);
        } else {
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
        PlayerData data = new PlayerData(owner);
        playerDataMap.put(owner, data);
        return data;
    }

    public boolean hasOnlinePlayerData(UUID owner) {
        if (playerDataMap.containsKey(owner)) {
            return playerDataMap.get(owner).getOwner() != null;
        }
        return getServer().getPlayer(owner) != null;
    }

    public void removePlayerData(UUID owner) {
        playerDataMap.remove(owner);
    }

}
