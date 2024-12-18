package ru.piko.pikopluginlib

import org.bukkit.NamespacedKey
import org.bukkit.plugin.java.JavaPlugin
import ru.piko.pikopluginlib.Commands.AbstractHelper
import ru.piko.pikopluginlib.Commands.CommandManager
import ru.piko.pikopluginlib.Commands.DefaultHelper
import ru.piko.pikopluginlib.Commands.Gamerules.GameRuleStandardSave
import ru.piko.pikopluginlib.Functions.FunctionAbstract.Static.destroyAll
import ru.piko.pikopluginlib.PlayersData.PlayerData
import ru.piko.pikopluginlib.PlayersData.PlayerDataRegistry
import ru.piko.pikopluginlib.Utils.PikoVariables.main
import java.io.File
import java.util.*

abstract class PikoPlugin : JavaPlugin() {
	// <editor-fold defaultstate="collapsed" desc="Variables">
	/**
	 * Manages the standard save state of game rules.
	 */
	private var gameRuleStandardSave: GameRuleStandardSave? = null
	
	private val commandManagerMap = mutableMapOf<String, CommandManager>()
	
	protected open val blocked: Boolean = false
	
	open val playerDataRegistry: Map<String, PlayerDataRegistry>
		get() = main.playerDataRegistry
	
	/**
	 * Unique identifier for the plugin.
	 * Format - "namespace.author.plugin_name"; Example "ru.piko.lib"
	 */
	protected lateinit var pluginId: String
	
	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Abstracts">
	/**
	 * Returns the unique identifier for this plugin.
	 *
	 * @return The plugin's unique ID.
	 */
	abstract fun getId(): String
	
	/**
	 * Called when the plugin is starting up. Should be overridden to define startup behavior.
	 */
	abstract fun onStart()
	
	/**
	 * Called when the plugin is shutting down. Should be overridden to define shutdown behavior.
	 */
	abstract fun onStop()
	abstract fun onRegister()
	
	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="onEnable & onDisable">
	/**
	 * Called by Bukkit when the plugin is enabled. Initializes the plugin ID and calls [.onStart].
	 */
	override fun onEnable() {
		registerPikoLib()
		try {
			onStart()
		} catch (e: Exception) {
			main.logger.warning("[ERROR] Plugin - " + getId() + " in onRegister error message: " + e.message + " stack track:")
			e.printStackTrace()
		}
	}
	
	fun registerPikoLib() {
		this.pluginId = getId()
		val plugin = Main.getPlugin()
		if (plugin != null) {
			plugin.addPikoPlugin(pluginId, this)
		} else {
			logger.warning("[PluginPikoLib] Main class not found!")
		}
		try {
			onRegister()
		} catch (e: Exception) {
			main.logger.warning("[ERROR] Plugin - " + getId() + " in onRegister error message: " + e.message + " stack track:")
			e.printStackTrace()
		}
	}
	
	/**
	 * Called by Bukkit when the plugin is disabled. Calls [.onStop] and removes the plugin from the main registry.
	 */
	override fun onDisable() {
		try {
			onStop()
		} catch (e: Exception) {
			main.logger.warning("[ERROR] Plugin - " + getId() + " in onStop error message: " + e.message + " stack track:")
			e.printStackTrace()
		}
		destroyAll(this)
		main.disablePikoPlugin(pluginId)
	}
	
	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Command Manager">
	/**
	 * Creates a CommandManager for handling commands related to this plugin.
	 *
	 * @param mainCommand The main command associated with the CommandManager.
	 * @return A new instance of CommandManager.
	 * @throws IllegalArgumentException if the command is not registered.
	 */
	fun createCommandManager(mainCommand: String, helper: AbstractHelper = DefaultHelper()): CommandManager {
		val commandManager = CommandManager(pluginId, mainCommand, helper)
		val command = this.getCommand(mainCommand)
		if (command != null) {
			command.setExecutor(commandManager)
			commandManagerMap[mainCommand] = commandManager
		} else {
			throw IllegalArgumentException("Command '$mainCommand' is not registered.")
		}
		return commandManager
	}
	
	fun getOrCreateCommandManager(mainCommand: String): CommandManager {
		if (hasCommandManager(mainCommand)) {
			return getCommandManager(mainCommand)!!
		}
		return createCommandManager(mainCommand)
	}
	
	fun getCommandManager(mainCommand: String): CommandManager? {
		if (commandManagerMap.containsKey(mainCommand)) {
			return commandManagerMap[mainCommand]
		}
		return null
	}
	
	fun hasCommandManager(mainCommand: String): Boolean {
		return commandManagerMap.containsKey(mainCommand)
	}
	
	fun addCommandManager(mainCommand: String, manager: CommandManager) {
		if (!commandManagerMap.containsKey(mainCommand)) {
			commandManagerMap[mainCommand] = manager
		} else {
			commandManagerMap.replace(mainCommand, manager)
		}
	}
	
	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Piko Plugin">
	/**
	 * Attempts to retrieve a PikoPlugin by its ID.
	 *
	 * @param id The ID of the plugin to retrieve.
	 * @return The PikoPlugin instance, or null if not found.
	 */
	@Deprecated("see {@link #tryGetPikoPluginData(String)}")
	fun tryGetPikoPlugin(id: String): Optional<PikoPlugin> {
		if (main.hasPikoPlugin(id)) {
			return Optional.of(main.getPikoPlugin(id)!!)
		}
		return Optional.empty()
	}
	
	/**
	 * Retrieves a PikoPlugin by its ID.
	 *
	 * @param id The ID of the plugin to retrieve.
	 * @return The PikoPlugin instance.
	 */
	@Deprecated("see {@link #getPikoPluginData(String)}")
	open fun getPikoPlugin(id: String): PikoPlugin? {
		return main.getPikoPlugin(id)
	}
	
	/**
	 * Checks if a PikoPlugin with the specified ID exists.
	 *
	 * @param id The ID of the plugin to check.
	 * @return True if the plugin exists, false otherwise.
	 */
	open fun hasPikoPlugin(id: String): Boolean {
		return main.hasPikoPlugin(id)
	}
	
	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Piko Plugin Data">
	/**
	 * Retrieves a PikoPlugin by its ID.
	 *
	 * @param id The ID of the plugin to retrieve.
	 * @return The PikoPluginData instance.
	 */
	open fun getPikoPluginData(id: String): PikoPluginData? {
		return main.getPikoPluginData(id)
	}
	
	/**
	 * Attempts to retrieve a PikoPlugin by its ID.
	 *
	 * @param id The ID of the plugin to retrieve.
	 * @return The PikoPlugin instance, or null if not found.
	 */
	fun tryGetPikoPluginData(id: String): Optional<PikoPluginData> {
		if (main.hasPikoPlugin(id)) {
			return Optional.of(main.getPikoPluginData(id)!!)
		}
		return Optional.empty()
	}
	
	val status: EStatusPlugin
		get() = main.getPikoPluginData(pluginId)?.status ?: EStatusPlugin.UNREGISTERED
	
	val pluginFile: File
		get() = file
	
	/**
	 * Creates a NamespacedKey for this plugin using the provided ID.
	 *
	 * @param id The ID to use in the NamespacedKey.
	 * @return A new NamespacedKey associated with this plugin.
	 */
	fun getNamespacedKey(id: String): NamespacedKey {
		return NamespacedKey(this, id)
	}
	
	val orCreateGameRuleStandardSave: GameRuleStandardSave
		/**
		 * Retrieves the GameRuleStandardSave instance, creating it if it doesn't exist.
		 *
		 * @return The GameRuleStandardSave instance associated with this plugin.
		 */
		get() {
			if (gameRuleStandardSave == null) {
				gameRuleStandardSave = GameRuleStandardSave(pluginId)
			}
			return gameRuleStandardSave!! // Безопасно!!
		}
	
	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Player Data">
	open fun hasOnlinePlayerData(owner: UUID): Boolean {
		return main.hasOnlinePlayerData(owner)
	}
	
	open fun getPlayerData(owner: UUID): PlayerData {
		return main.getPlayerData(owner)
	}
	
	open fun removePlayerData(owner: UUID) {
		main.removePlayerData(owner)
	}
	
	// <editor-fold-sub defaultstate="collapsed" desc="">
	open fun registerPlayerData(id: String, registry: PlayerDataRegistry) {
		main.registerPlayerData(id, registry)
	}
	
	open fun unregisterPlayerData(id: String) {
		main.unregisterPlayerData(id)
	}
	// </editor-fold-sub>
	// </editor-fold>
}