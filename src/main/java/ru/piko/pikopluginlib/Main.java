package ru.piko.pikopluginlib;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.piko.pikopluginlib.Listeners.MenuEvent;
import ru.piko.pikopluginlib.PlayersData.PlayerData;

import java.util.*;

public final class Main extends PikoPlugin {

    private static Main plugin;

    private final Map<String, PikoPluginData> pikoPluginDataMap = new HashMap<>();
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
    public void onRegister() {}

    @Override
    public void onEnable() {
        plugin = this;
        this.pluginId = getPluginId();
        addPikoPlugin(this.pluginId, this, true);
        System.out.println("PikoPluginLib load!");
        getServer().getPluginManager().registerEvents(new MenuEvent(), this);

        //getOrCreateCommandManager("piko");

        for (Plugin plugin : getServer().getPluginManager().getPlugins()) {
            if (plugin != this && plugin instanceof JavaPlugin javaPlugin && javaPlugin instanceof PikoPlugin pikoPlugin) {
                // found a PikoPlugin instance, do something with it
                pikoPlugin.registerPikoLib();
            }
        }
    }

    public static Main getPlugin() {
        return plugin;
    }

    // <editor-fold defaultstate="collapsed" desc="Piko Plugin Data">

    public PikoPluginData getPikoPluginData(@NotNull String id) {
        return pikoPluginDataMap.get(id);
    }

    public void disablePikoPlugin(@NotNull String id) {
        if (pikoPluginDataMap.containsKey(id)) {
            PikoPluginData data = pikoPluginDataMap.get(id);
            if (data.getStatus().isBlocked() || data.getStatus().isDisable()) return;
            data.setPlugin(null);
            data.setStatus(EStatusPlugin.DISABLE);
        }
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Piko Plugin">

    public void addDisablePikoPlugin(@NotNull String id) {
        if (!pikoPluginDataMap.containsKey(id)) {
            pikoPluginDataMap.put(id, new PikoPluginData(id));
        }
    }

    public void addPikoPlugin(@NotNull String id, @NotNull PikoPlugin pikoPlugin) {
        addPikoPlugin(id, pikoPlugin, false);
    }

    public void addPikoPlugin(@NotNull String id, @NotNull PikoPlugin pikoPlugin, boolean blocked) {
        if (pikoPluginDataMap.containsKey(id)) {
            PikoPluginData data = pikoPluginDataMap.get(id);
            if (data.getStatus().isEnable()) {
                System.out.println("PikoPlugin with id: " + id + " already registered.");
            } else {
                data.activate(pikoPlugin, blocked);
            }
        } else {
            pikoPluginDataMap.put(id, new PikoPluginData(id, pikoPlugin, blocked));
        }
    }
    /**
     * @deprecated see {@link #getPikoPluginData(String)}
     */
    @Deprecated
    public PikoPlugin getPikoPlugin(@NotNull String id) {
        return pikoPluginDataMap.get(id).getPlugin();
    }
    public boolean hasPikoPlugin(@NotNull String id) {
        return pikoPluginDataMap.containsKey(id) && pikoPluginDataMap.get(id).getStatus().isEnable();
    }

    /**
     * @deprecated see {@link #disablePikoPlugin(String)}
     */
    @Deprecated
    public void removePikoPlugin(@NotNull String id) {
        disablePikoPlugin(id);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Player Data">
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
    // </editor-fold>

    public Map<String, PikoPluginData> getPikoPlugins() {
        return pikoPluginDataMap;
    }

}
