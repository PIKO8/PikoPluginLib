package ru.piko.pikopluginlib.Listeners

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.plugin.java.JavaPlugin
import ru.piko.pikopluginlib.Functions.FunctionTimer
import ru.piko.pikopluginlib.Api.PikoPlugin
import ru.piko.pikopluginlib.Utils.InternalObject.main

class PluginListener : Listener {
	
	@EventHandler
	fun onDisablePlugin(e: PluginDisableEvent) {
		val plugin = e.plugin
		if (plugin is JavaPlugin && plugin is PikoPlugin) {
			
			// Проверка, что плагин не в процессе первичной загрузки
			if (plugin.pluginLoadingInProgress) return
			
			var data = main.api.plugins.get(plugin.id)
			if (data == null) {
				main.logger.warning("How could this even happen???!!! A plugin that is not in the system is disabled. How did it bypass the activation? is the PikoPluginLib duplicate shaded?")
				main.api.plugins.addDisable(plugin.id)
				data = main.api.plugins.get(plugin.id)
			} else {
				if (!data.status.isDisable) {
					if (data.plugin != plugin) {
						throw IllegalStateException("Identical IDs for different instances! One: ${data.file.toString()}; Two: ${plugin.pluginFile}")
					}
				}
				if (data.status.isBlocked) {
					data.disable()
					main.logger.info("The ${data.namePlugin} plugin is disabled. The status in the PikoPlugins system is set to 'BLOCKED_DISABLE'.")
					
					try {
						error("call catch")
					} catch (_: Exception) {
						throw IllegalStateException("Disabling a blocked plugin. Blocked plugins cannot be disabled!")
					} finally {
						Bukkit.shutdown()
					}
				}
			}
			val name = data?.namePlugin ?: "???"
			main.logger.info("The $name plugin is disabled. The status in the PikoPlugins system is set to disable.")
		}
	}
	
}