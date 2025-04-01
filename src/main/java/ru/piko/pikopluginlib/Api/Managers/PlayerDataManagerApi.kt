package ru.piko.pikopluginlib.Api.Managers

import org.bukkit.Bukkit
import ru.piko.pikopluginlib.PlayersData.APlayerData
import ru.piko.pikopluginlib.PlayersData.IPlayerDataRegistry
import ru.piko.pikopluginlib.PlayersData.PlayerData
import java.util.*

class PlayerDataManagerApi internal constructor() {
	private val playerDataMap = mutableMapOf<UUID, PlayerData>()
	private val playerDataRegistry = mutableMapOf<String, IPlayerDataRegistry>()
	
	fun get(owner: UUID): PlayerData {
		return playerDataMap.getOrPut(owner) { PlayerData(owner) }
	}
	
	fun isOnline(uuid: UUID): Boolean {
		return playerDataMap[uuid]?.isOnline ?: (Bukkit.getPlayer(uuid) != null)
	}
	
	fun remove(owner: UUID) {
		playerDataMap.remove(owner)
	}
	
	fun register(registry: IPlayerDataRegistry) {
		playerDataRegistry[registry.id] = registry
	}
	
	fun unregister(id: String) {
		playerDataRegistry.remove(id)
	}
	
	fun clearStartWith(str: String, ignoreCase: Boolean = false) {
		playerDataMap.forEach { (_, data) -> data.clearStartWith(str, ignoreCase) }
	}
	
	fun clearFunction(function: (Map.Entry<String, APlayerData>) -> Boolean) {
		playerDataMap.forEach { (_, data) -> data.clearFunction(function) }
	}
	
	val map: Map<UUID, PlayerData> get() = playerDataMap.toMap()
	val registryMap: Map<String, IPlayerDataRegistry> get() = playerDataRegistry.toMap()
	
	
}