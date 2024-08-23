package ru.piko.pikopluginlib.PlayersData;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PlayerData {
    private final Player owner;

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

    public PlayerData(Player owner) {
        this.owner = owner;
        this.playerDataMap = new HashMap<>();
    }

    public Player getOwner() {
        return owner;
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
}