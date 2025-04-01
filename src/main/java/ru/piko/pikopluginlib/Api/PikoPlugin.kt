package ru.piko.pikopluginlib.Api

import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.ApiStatus
import ru.piko.pikopluginlib.Api.Linked.LinkedPikoLibApi
import ru.piko.pikopluginlib.Functions.FunctionAbstract.Companion.destroyAll
import ru.piko.pikopluginlib.Utils.AutoRegisterObject
import ru.piko.pikopluginlib.Utils.Extends.Language.LoggerExtend.infoInline
import ru.piko.pikopluginlib.Utils.Extends.Language.ThrowableExtend.bukkitSevere
import ru.piko.pikopluginlib.Utils.InternalObject.main
import java.io.File

typealias PikoPluginAny = PikoPlugin<*>

abstract class PikoPlugin<Impl: PikoPlugin<Impl>> : JavaPlugin() {
	// <editor-fold defaultstate="collapsed" desc="Variables">
	open val blocked: Boolean = false
	
	var pluginLoadingInProgress = true
	
	@get:JvmName("api")
	val api get() = with(PikoPluginLibApi) { if (isInit) this else null } ?: error("PikoPluginLibApi is not registered")
	
	@get:JvmName("data")
	val data: PikoPluginData
		get() = api.plugins.get(pluginId) ?: error("PikoPlugin#data was used when the plugin '$pluginId' was not registered")
	
	@get:JvmName("isFirstLoad")
	val isFirstLoad: Boolean get() = api.plugins.get(pluginId)?.isFirstLoad() ?: true
//
//	internal var internalLinkApi: LinkedPikoLibApi<Impl>? = null
//
//	val linkApi: LinkedPikoLibApi<Impl> get() = internalLinkApi ?: error("LinkApi not registered!")
	
	open val isAutoRegister: Boolean = false
	
	open val autoRegisterPackage: String = ""
	
	/**
	 * Unique identifier for the plugin.
	 * Format - "namespace.author.plugin_name"; Example "ru.piko.lib"
	 */
	protected lateinit var pluginId: String
	
	@get:JvmName("pluginFile")
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
	open fun onStart() {
		logger.infoInline { "Plugin Start!" }
	}
	
	/**
	 * Called when the plugin is shutting down. Should be overridden to define shutdown behavior.
	 */
	open fun onStop() {
		logger.infoInline { "Plugin Stop!" }
	}
	
	open fun onRegister(isFirstLoad: Boolean) {}
	// </editor-fold>
	
	// <editor-fold defaultstate="collapsed" desc="onEnable & onDisable">
	private fun registerPikoLib(): Boolean {
		this.pluginId = id
		if (!api.isInit) {
			logger.severe("[PluginPikoLib] is not load!")
			return true
		}
		api.plugins.add(pluginId, this)
		return false
	}
	
	@ApiStatus.NonExtendable
	override fun onLoad() {
		pluginLoadingInProgress = true
		if (registerPikoLib()) {
			pluginLoadingInProgress = false
			return
		}
//		internalLinkApi = LinkedPikoLibApi(this as Impl) TODO
		try {
			onRegister(isFirstLoad)
		} catch (e: Exception) {
			e.bukkitSevere(main, "Plugin - $pluginId in the 'onRegister' method caused an error.")
//			main.logger.severe("Plugin - $pluginId in the 'onRegister' method caused an error, stack track:")
//			e.printStackTrace()
		}
		if (isAutoRegister) {
			val trimed = autoRegisterPackage.trim()
			if (trimed.isNotEmpty()) {
				AutoRegisterObject.load(this, classLoader, trimed)
			}
		}
		pluginLoadingInProgress = false
	}
	
	/**
	 * Called by Bukkit when the plugin is enabled. Initializes the plugin ID and calls [onStart].
	 */
	@ApiStatus.NonExtendable
	final override fun onEnable() {
		pluginLoadingInProgress = true
		if (!PikoPluginLibApi.isInit) {
			logger.severe("[PikoPluginLib] calling 'OnEnable' when PikoPluginLib is not initialized")
			return
		} else if (api.plugins.get(pluginId) == null || !data.status.isEnable) {
			main.logger.severe("Calling 'OnEnable' when [$name] is not enable")
			return
		}
		try {
			onStart()
		} catch (e: Exception) {
			e.bukkitSevere(main, "Plugin - $pluginId in the 'onStart' method caused an error.")
//			main.logger.severe("Plugin - $pluginId in the 'onStart' method caused an error, stack track:")
//			e.printStackTrace()
		}
		data.addCount()
		pluginLoadingInProgress = false
	}
	
	/**
	 * Called by Bukkit when the plugin is disabled. Calling [onStop] and removes the plugin from the main registry.
	 */
	@ApiStatus.NonExtendable
	final override fun onDisable() {
		try {
			onStop()
		} catch (e: Exception) {
			e.bukkitSevere(main, "Plugin - $pluginId in the 'onStop' method caused an error.")
//			main.logger.warning("[ERROR] Plugin - $pluginId in the 'onStop' method caused an error, stack track:")
//			e.printStackTrace()
		}
		destroyAll(this)
		api.plugins.disable(id)
	}
	// </editor-fold>
	
}