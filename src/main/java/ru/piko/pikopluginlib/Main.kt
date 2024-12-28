package ru.piko.pikopluginlib

import ru.piko.pikopluginlib.Commands.SubCommands.ReloadSubCommand
import ru.piko.pikopluginlib.Files.Release.Yaml.MyPluginFolder
import ru.piko.pikopluginlib.Listeners.MenuListener
import ru.piko.pikopluginlib.PlayersData.PlayerData
import ru.piko.pikopluginlib.PlayersData.PlayerDataRegistry
import ru.piko.pikopluginlib.Utils.InternalObject.main
import ru.piko.pikopluginlib.Listeners.PluginListener
import ru.piko.pikopluginlib.Listeners.PlayerListener
import ru.piko.pikopluginlib.PlayersData.APlayerData
import ru.piko.pikopluginlib.Utils.Test
import java.util.*

class Main : PikoPlugin() {
	
	companion object {
		private var plugin: Main? = null
		
		fun getPlugin(): Main? = plugin
	}
	
	
	private val pikoPluginDataMap = mutableMapOf<String, PikoPluginData>()
	private val playerDataMap = mutableMapOf<UUID, PlayerData>()
	override val playerDataRegistry = mutableMapOf<String, PlayerDataRegistry>()
	
	override fun getId(): String = "ru.piko.lib"
	
	override fun onStart() {
		// TestFunctions.Static.test(this)
		// TestFunctions.Static.complexFunctionBuilderExample(this)
		// Test.INSTANCE.test()
		
		
		server.pluginManager.registerEvents(MenuListener(), this)
		server.pluginManager.registerEvents(PluginListener(), this)
		server.pluginManager.registerEvents(PlayerListener(), this)
		
		getOrCreateCommandManager("piko").addCommand(ReloadSubCommand())
		
		//MyPluginFolder()
	}
	
	override fun onStop() {}
	override fun onRegister() {}
	
	override fun onEnable() {
		pluginLoadingInProgress = true
		plugin = this
		this.pluginId = getId()
		addPikoPlugin(this.pluginId, this, true)
		
		
		try {
			onStart() // остальной запуск
			println("PikoPluginLib load!")
		} catch (t: Throwable) {
			t.printStackTrace()
			println("PikoPluginLib error onStart!")
		}
		
		pluginLoadingInProgress = false
	}
	
	override fun getPikoPluginData(id: String): PikoPluginData? = pikoPluginDataMap[id]
	
	fun disablePikoPlugin(id: String) {
		pikoPluginDataMap[id]?.let { data ->
			if (!data.status.isDisable) {
				data.disable()
			}
		}
	}
	
	fun addDisablePikoPlugin(id: String) {
		if (id !in pikoPluginDataMap) {
			pikoPluginDataMap[id] = PikoPluginData(id)
		}
	}
	
	fun addPikoPlugin(id: String, pikoPlugin: PikoPlugin, blocked: Boolean = false) {
		(pikoPluginDataMap[id]).let { data ->
			val status = data?.status ?: EStatusPlugin.UNREGISTERED
			when {
				data == null || status.isUnregistered -> {
					pikoPluginDataMap[id] = PikoPluginData(id, pikoPlugin, blocked)
				}
				status.isEnable -> {
					println("PikoPlugin with ID: $id already registered.")
				}
				status.isBlocked -> {
					main.logger.warning("The plugin with ID: $id, which has a lock state, has probably been reloaded. This means that it is NOT RECOMMENDED to restart this plugin. Tip: restart the server completely.")
					data.activate(pikoPlugin, true)
				}
				status.isDisable -> {
					println("PikoPlugin with ID: $id is enabled")
					data.activate(pikoPlugin, blocked)
				}
			}
		}
	}
	
	@Deprecated("see {@link #getPikoPluginData(String)}")
	override fun getPikoPlugin(id: String): PikoPlugin? = pikoPluginDataMap[id]?.plugin
	
	override fun hasPikoPlugin(id: String): Boolean = pikoPluginDataMap[id]?.status?.isEnable == true
	
	@Deprecated("see {@link #disablePikoPlugin(String)}")
	fun removePikoPlugin(id: String) {
		disablePikoPlugin(id)
	}
	
	override fun getPikoPlugins(): Map<String, PikoPluginData> = pikoPluginDataMap
	
	// <editor-fold defaultstate="collapsed" desc="Player Data">
	override fun getPlayerData(owner: UUID): PlayerData {
		return playerDataMap.getOrPut(owner) { PlayerData(owner) }
	}
	
	override fun hasOnlinePlayerData(owner: UUID): Boolean {
		return playerDataMap[owner]?.owner != null || server.getPlayer(owner) != null
	}
	
	override fun removePlayerData(owner: UUID) {
		playerDataMap.remove(owner)
	}
	
	override fun registerPlayerData(id: String, registry: PlayerDataRegistry) {
		playerDataRegistry[id] = registry
	}
	
	override fun unregisterPlayerData(id: String) {
		playerDataRegistry.remove(id)
	}
	
	override fun clearStartWith(str: String, ignoreCase: Boolean) {
		playerDataMap.forEach { (_, data) -> data.clearStartWith(str, ignoreCase) }
	}
	
	override fun clearFunction(function: (Map.Entry<String, APlayerData>) -> Boolean) {
		playerDataMap.forEach { (_, data) -> data.clearFunction(function) }
	}
	
	override fun getPlayerData(): Map<UUID, PlayerData> = playerDataMap
	
	// </editor-fold>
	
}