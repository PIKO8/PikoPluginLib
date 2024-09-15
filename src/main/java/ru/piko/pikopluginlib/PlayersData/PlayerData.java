package ru.piko.pikopluginlib.PlayersData;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class PlayerData {
    private final UUID uuid;

    /**
     * A map that stores player-specific data for various plugins and subsystems.
     *
     * <p>The key is a string formatted as "<code>plugin_id.subsystem_id.name</code>", where:</p>
     * <ul>
     *   <li><strong>plugin_id</strong>: The unique identifier for the plugin (e.g., "magic_piko").</li>
     *   <li><strong>subsystem_id</strong>: The identifier for a specific subsystem within the plugin, such as "menu" or "command".</li>
     *   <li><strong>name</strong>: The name or further identifier of the data (e.g., "staff").</li>
     * </ul>
     *
     * <p>Example key: "magic_piko.menu.staff"</p>
     *
     * <p>The value is an instance of {@link APlayerData}, which represents the data associated with that specific plugin and subsystem for the player.</p>
     */
    private final Map<String, APlayerData> playerDataMap;

    public PlayerData(UUID owner) {
        this.uuid = owner;
        this.playerDataMap = new HashMap<>();
    }

    public UUID getUUID() {
        return uuid;
    }

    public boolean hasPlayerOnline() {
        return getOwner() != null;
    }

    public Player getOwner() {
        return Bukkit.getPlayer(uuid);
    }

    public void addData(String id, APlayerData data) {
        playerDataMap.put(id, data);
    }

    public <T extends APlayerData> Optional<T> tryGetData(String id) {
        if (hasData(id)) {
            return Optional.of((T) getData(id));
        }
        return Optional.empty();
    }

    public APlayerData getData(String id) {
        return playerDataMap.get(id);
    }

    public boolean hasData(String id) {
        return playerDataMap.containsKey(id);
    }

    public void removeData(String id) {
        playerDataMap.remove(id);
    }

    /**
     * Retrieves existing data or creates it if absent.
     *
     * @param id The identifier for the data.
     * @param factory The factory method to create the data if it doesn't exist.
     * @param <T> The type of the data.
     * @return The existing or newly created data.
     */
    public <T extends APlayerData> T getOrCreateData(String id, Supplier<T> factory) {
        return (T) playerDataMap.computeIfAbsent(id, k -> factory.get());
    }
}