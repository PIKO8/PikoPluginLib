package ru.piko.pikopluginlib

import ru.piko.pikopluginlib.Api.PikoPluginLibApi
import ru.piko.pikopluginlib.Commands.SubCommands.ReloadSubCommand
import ru.piko.pikopluginlib.Listeners.MenuListener
import ru.piko.pikopluginlib.Listeners.PlayerListener
import ru.piko.pikopluginlib.Listeners.PluginListener

class Main : PikoPlugin() {
	
	companion object {
		private var plugin: Main? = null
		
		fun getPlugin(): Main? = plugin
	}
	
	
	override val id: String = "ru.piko.lib"
	
	override fun onEnable() {
		pluginLoadingInProgress = true
		plugin = this
		this.pluginId = id
		
		PikoPluginLibApi.init()
		
		PikoPluginLibApi.plugins.add(this.pluginId, this, true)
		
		
		try {
			onStart() // остальной запуск
			println("PikoPluginLib load!")
		} catch (t: Throwable) {
			t.printStackTrace()
			println("PikoPluginLib error onStart!")
		}
		
		pluginLoadingInProgress = false
	}
	override fun onStart() {
		server.pluginManager.registerEvents(MenuListener(), this)
		server.pluginManager.registerEvents(PluginListener(), this)
		server.pluginManager.registerEvents(PlayerListener(), this)
		
		api.commands.getOrCreate("piko").addCommand(ReloadSubCommand())
	}
	
	override fun onStop() {}
	override fun onRegister() {}
	
}