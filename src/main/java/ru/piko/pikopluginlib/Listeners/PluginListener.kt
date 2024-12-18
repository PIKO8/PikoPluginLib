package ru.piko.pikopluginlib.Listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.plugin.java.JavaPlugin
import ru.piko.pikopluginlib.PikoPlugin
import ru.piko.pikopluginlib.Utils.PikoVariables.main

class PluginListener : Listener {
	
	// Оказывается не требуется потому что при включении плагина и так происходит вся регистрация!
//	@EventHandler
//	fun onEnablePlugin(e: PluginEnableEvent) {
//		val plugin = e.plugin
//		if (plugin is JavaPlugin && plugin is PikoPlugin) {
//			if (!main.hasPikoPlugin(plugin.getPluginId())) {
//				val data = main.getPikoPluginData(plugin.getPluginId())
//				data?.activate(plugin, false)
//			} else {
//				main.addPikoPlugin(plugin.getPluginId(), plugin, false)
//			}
//		}
//	}
	
	@EventHandler
	fun onDisablePlugin(e: PluginDisableEvent) {
		val plugin = e.plugin
		if (plugin is JavaPlugin && plugin is PikoPlugin) {
			var data = main.getPikoPluginData(plugin.getId())
			if (data == null) {
				main.logger.warning("How could this even happen???!!! A plugin that is not in the system is disabled. How did it bypass the activation? is the PikoPluginLib duplicate shaded?")
				main.addDisablePikoPlugin(plugin.getId())
				data = main.getPikoPluginData(plugin.getId())
			} else {
				if (!data.status.isDisable) {
					if (data.plugin != plugin) {
						throw IllegalStateException("Identical IDs for different instances! One: ${data.file.toString()}; Two: ${plugin.pluginFile}")
					}
				}
				if (data.status.isBlocked) {
					data.disable()
					main.logger.info("The ${data.namePlugin} plugin is disabled. The status in the PikoPlugins system is set to 'BLOCKED_DISABLE'.")
					throw IllegalStateException("Disabling a blocked plugin. Blocked plugins cannot be disabled!!!")
				}
			}
			val name = data?.namePlugin ?: "???"
			main.logger.info("The $name plugin is disabled. The status in the PikoPlugins system is set to disable.")
		}
	}
	
}