package ru.piko.pikopluginlib.Api

import org.bukkit.plugin.java.JavaPlugin
import ru.piko.pikopluginlib.Files.Abstract.folder.AbstractPikoPluginFolder
import ru.piko.pikopluginlib.Functions.FunctionAbstract.Static.destroyAll
import ru.piko.pikopluginlib.Utils.InternalObject.main
import java.io.File

abstract class PikoPlugin : JavaPlugin() {
	// <editor-fold defaultstate="collapsed" desc="Variables">
	protected open val blocked: Boolean = false
	
	var pluginLoadingInProgress = true
	
	var folder: AbstractPikoPluginFolder<*>? = null
	
	val api = PikoPluginLibApi
	
	/**
	 * Unique identifier for the plugin.
	 * Format - "namespace.author.plugin_name"; Example "ru.piko.lib"
	 */
	protected lateinit var pluginId: String
	
	val pluginFile: File get() = this.file
	
	// </editor-fold>
	
	// <editor-fold defaultstate="collapsed" desc="Abstracts">
	/**
	 * Unique identifier for the plugin.
	 * Format - "namespace.author.plugin_name"; Example "ru.piko.lib"
	 *
	 * @return The plugin's unique ID.
	 */
	abstract val id: String
	
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
		pluginLoadingInProgress = true
		if (registerPikoLib()) {
			pluginLoadingInProgress = false
			return
		}
		try {
			onStart()
		} catch (e: Exception) {
			main.logger.warning("[ERROR] Plugin - " + pluginId + " in onStart error message: " + e.message + " stack track:")
			e.printStackTrace()
		}
		pluginLoadingInProgress = false
	}
	
	internal fun registerPikoLib(): Boolean {
		this.pluginId = id
		if (!api.isInit) {
			logger.warning("[PluginPikoLib] is not load!")
			return true
		}
		api.plugins.add(pluginId, this)
		try {
			onRegister()
		} catch (e: Exception) {
			main.logger.warning("[ERROR] Plugin - " + id + " in onRegister error message: " + e.message + " stack track:")
			e.printStackTrace()
		} finally {
			return false
		}
	}
	
	/**
	 * Called by Bukkit when the plugin is disabled. Calls [.onStop] and removes the plugin from the main registry.
	 */
	override fun onDisable() {
		try {
			onStop()
		} catch (e: Exception) {
			main.logger.warning("[ERROR] Plugin - " + id + " in onStop error message: " + e.message + " stack track:")
			e.printStackTrace()
		}
		destroyAll(this)
		api.plugins.disable(id)
	}
	
	// </editor-fold>
	
}