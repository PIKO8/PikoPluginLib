package ru.piko.pikopluginlib

import ru.piko.pikopluginlib.Commands.SubCommands.ReloadSubCommand
import ru.piko.pikopluginlib.Listeners.MenuListener
import ru.piko.pikopluginlib.Listeners.PluginListener
import ru.piko.pikopluginlib.PlayersData.PlayerData
import ru.piko.pikopluginlib.PlayersData.PlayerDataRegistry
import ru.piko.pikopluginlib.Utils.PikoVariables.main
import ru.piko.pikopluginlib.listeners.PlayerListener
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
	}
	
	override fun onStop() {}
	override fun onRegister() {}
	
	override fun onEnable() {
		plugin = this
		this.pluginId = getId()
		addPikoPlugin(this.pluginId, this, true)
		println("PikoPluginLib load!")
		server.pluginManager.registerEvents(MenuListener(), this)
		server.pluginManager.registerEvents(PluginListener(), this)
		server.pluginManager.registerEvents(PlayerListener(), this)
		
		getOrCreateCommandManager("piko").addCommand(ReloadSubCommand())
		onStart()
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
	
	fun getPikoPlugins(): Map<String, PikoPluginData> = pikoPluginDataMap
}