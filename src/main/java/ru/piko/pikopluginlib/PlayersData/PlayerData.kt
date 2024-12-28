package ru.piko.pikopluginlib.PlayersData

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.*

class PlayerData(val uuid: UUID) {
	/**
	 * A map that stores player-specific data for various plugins and subsystems.
	 *
	 *
	 * The key is a string formatted as "`author.plugin.name`", where:
	 *
	 *  * **author**: The nickname of the plugin author or the name of the team (e.g., "piko")
	 *  * **plugin**: The unique identifier for the plugin (e.g., "magic").
	 *  * **name**: The name or further identifier of the data (e.g., "staff").
	 *
	 *
	 *
	 * Example key: "piko.magic.staff"
	 *
	 *
	 * The value is an instance of [APlayerData], which represents the data associated with that specific plugin and subsystem for the player.
	 */
	val playerDataMap: MutableMap<String, APlayerData> = HashMap()
	
	val isOnline: Boolean get() = offlineOwner.isOnline
	
	val offlineOwner: OfflinePlayer
		get() = Bukkit.getOfflinePlayer(uuid)
	
	val owner: Player?
		get() = Bukkit.getPlayer(uuid)
	
	fun addData(id: String, data: APlayerData) {
		playerDataMap[id] = data
	}
	
	fun <T : APlayerData?> tryGetData(id: String): Pair<Boolean, T?> {
		if (hasData(id)) {
			val value = getData(id) as? T?
			return Pair(value != null, value)
		}
		return Pair(false, null)
	}
	
	fun getData(id: String): APlayerData? {
		return playerDataMap[id]
	}
	
	fun hasData(id: String): Boolean {
		return playerDataMap.containsKey(id)
	}
	
	fun removeData(id: String) {
		playerDataMap.remove(id)
	}
	
	fun clearStartWith(str: String, ignoreCase: Boolean = false) {
		playerDataMap.keys.filter { it.startsWith(str, ignoreCase) }.forEach { playerDataMap.remove(it) }
	}
	
	fun clearFunction(function: (Map.Entry<String, APlayerData>) -> Boolean) {
		playerDataMap.entries
			.filter(function)
			.map { it.key }
			.forEach { playerDataMap.remove(it) }
	}
	
	/**
	 * Retrieves existing data or creates it if absent.
	 *
	 * @param id The identifier for the data.
	 * @param factory The factory method to create the data if it doesn't exist.
	 * @param <T> The type of the data.
	 * @return The existing or newly created data.
	</T> */
	fun <T : APlayerData?> getOrCreateData(id: String, function: (String) -> APlayerData): T {
		return playerDataMap.computeIfAbsent(id, function) as T
	}
}