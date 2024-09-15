package ru.piko.pikopluginlib;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ru.piko.pikopluginlib.Items.ItemDataBuilder;
import ru.piko.pikopluginlib.Listeners.MenuEvent;
import ru.piko.pikopluginlib.PlayersData.PlayerData;

import java.util.*;

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
        addPikoPlugin(this.pluginId, this);
        System.out.println("PikoPluginLib load!");
        getServer().getPluginManager().registerEvents(new MenuEvent(), this);
    }

    public static Main getPlugin() {
        return plugin;
    }

    public void addPikoPlugin(@NotNull String pluginId, @NotNull PikoPlugin pikoPlugin) {
        if (!pikoPluginMap.containsKey(pluginId)) {
            pikoPluginMap.put(pluginId, pikoPlugin);
        } else {
            System.out.println("PikoPlugin with id: " + pluginId + " already registered.");
        }
    }

    public boolean hasPikoPlugin(@NotNull String id) {
        return pikoPluginMap.containsKey(id);
    }

    public PikoPlugin getPikoPlugin(@NotNull String id) {
        return pikoPluginMap.get(id);
    }

    public void removePikoPlugin(@NotNull String id) {
        pikoPluginMap.remove(id);
    }

    // PLAYER DATA
    public @NotNull PlayerData getPlayerData(@NotNull UUID owner) {
        if (playerDataMap.containsKey(owner)) {
            return playerDataMap.get(owner);
        }
        PlayerData data = new PlayerData(owner);
        playerDataMap.put(owner, data);
        return data;
    }

    public boolean hasOnlinePlayerData(@NotNull UUID owner) {
        if (playerDataMap.containsKey(owner)) {
            return playerDataMap.get(owner).getOwner() != null;
        }
        return getServer().getPlayer(owner) != null;
    }

    public void removePlayerData(@NotNull UUID owner) {
        playerDataMap.remove(owner);
    }

}
