package ru.piko.pikopluginlib;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import ru.piko.pikopluginlib.Items.ItemBuilder;
import ru.piko.pikopluginlib.Items.ItemBuilderData;
import ru.piko.pikopluginlib.Items.ItemBuilderNBT;
import ru.piko.pikopluginlib.Listeners.MenuEvent;
import ru.piko.pikopluginlib.PlayersData.APlayerData;
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
        addPikoPlugin(pluginId, this);
        getServer().getPluginManager().registerEvents(new MenuEvent(), this);
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
        Player player = Bukkit.getPlayer(owner);
        if (player == null) {
            return null;
        }
        PlayerData data = new PlayerData(player);
        playerDataMap.put(owner, data);
        return data;
    }

    public void removePlayerData(UUID owner) {
        playerDataMap.remove(owner);
    }

}
