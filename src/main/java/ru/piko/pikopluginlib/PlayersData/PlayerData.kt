package ru.piko.pikopluginlib.PlayersData

import io.netty.buffer.Unpooled
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import ru.piko.pikopluginlib.Api.PikoPluginLibApi
import ru.piko.pikopluginlib.Utils.ByteBufCodec
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
		val reg = PikoPluginLibApi.playerData.registryMap[id] ?: error("PlayerData id: '$id' is not registered")
		playerDataMap[id] = when (reg) {
			is CodecPlayerDataRegistry<*> -> {
				@Suppress("UNCHECKED_CAST")
				val codec = reg.codec as? ByteBufCodec<APlayerData> ?: error("PlayerData id: '$id'. The codec cannot be used.")
				val buffer = Unpooled.buffer()
				codec.encode(buffer, data)
				ByteBufPlayerData(id, buffer)
			}
			is CommonPlayerDataRegistry -> {
				data
			}
			else -> error("Unknown implementation of IPlayerDataRegistry: '${reg::class.java}'")
		}
	}
	
	fun <T : APlayerData?> tryGetData(id: String): Pair<Boolean, T?> {
		if (hasData(id)) {
			val value = getData(id) as? T?
			return Pair(value != null, value)
		}
		return Pair(false, null)
	}
	
	fun getData(id: String): APlayerData? {
		val reg = PikoPluginLibApi.playerData.registryMap[id] ?: error("PlayerData id: '$id' is not registered")
		if (playerDataMap.containsKey(id)) {
			val value = playerDataMap[id]
			if (value != null) when (reg)
			{
				is CodecPlayerDataRegistry<*> -> {
					if (value !is ByteBufPlayerData) return null
					val codec = reg.codec as? ByteBufCodec<APlayerData> ?: return null
					value.buffer.readerIndex(0)
					return codec.decode(value.buffer)
				}
				is CommonPlayerDataRegistry -> {
					return value
				}
				else -> error("Unknown implementation of IPlayerDataRegistry: '${reg::class.java}'")
			}
		}
		return null
	}
	
	fun hasData(id: String): Boolean {
		return playerDataMap.containsKey(id)
	}
	
	fun removeData(id: String) {
		playerDataMap.remove(id)
	}
	
	fun clearStartWith(str: String, ignoreCase: Boolean = false) {
		playerDataMap.keys.filter { it.startsWith(str, ignoreCase) }.forEach { playerDataMap.remove(it)?.also { also -> if (also is ByteBufPlayerData) also.buffer.release() } }
	}
	
	fun clearFunction(function: (Map.Entry<String, APlayerData>) -> Boolean) {
		playerDataMap.entries
			.filter(function)
			.map { it.key }
			.forEach { playerDataMap.remove(it)?.also { also -> if (also is ByteBufPlayerData) also.buffer.release() } }
	}
	
	/**
	 * Retrieves existing data or creates it if absent.
	 *
	 * @param id The identifier for the data.
	 * @param <T> The type of the data.
	 * @return The existing or newly created data.
	</T> */
	inline fun <reified T : APlayerData> getOrCreateData(id: String): T {
//		return playerDataMap.computeIfAbsent(id, function) as T
		val reg = PikoPluginLibApi.playerData.registryMap[id] ?: error("PlayerData id: '$id' is not registered")
		if (playerDataMap.containsKey(id)) {
			val value = playerDataMap[id]
			if (value != null) when (reg)
			{
				is CodecPlayerDataRegistry<*> -> {
					if (value !is ByteBufPlayerData) error("PlayerData id: '$id'. Registration using CodecPlayerDataRegistry involves storing ByteBufPlayerData, which is created and stored automatically, some kind of internal problem has occurred.")
					val codec = reg.codec as? ByteBufCodec<T> ?: error("PlayerData id: '$id'. The codec cannot be used.")
					value.buffer.readerIndex(0)
					return codec.decode(value.buffer)
				}
				is CommonPlayerDataRegistry -> {
					if (value is T) {
						return value as T
					}
				}
				else -> error("Unknown implementation of IPlayerDataRegistry: '${reg::class.java}'")
			}
		}
		val value = reg.create()
		addData(id, value)
		return value as T
	}
}