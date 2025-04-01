package ru.piko.pikopluginlib

import ru.piko.pikopluginlib.Api.PikoPlugin
import ru.piko.pikopluginlib.Api.PikoPluginLibApi
import ru.piko.pikopluginlib.Listeners.MenuListener
import ru.piko.pikopluginlib.Listeners.PlayerListener
import ru.piko.pikopluginlib.Listeners.PluginListener

class Main : PikoPlugin<Main>() {
	
	companion object {
		private var plugin: Main? = null
		
		fun getPlugin(): Main? = plugin
	}
	
	override val blocked: Boolean = true
	
	override val id: String = "ru.piko.lib"
	
	override fun onLoad() {
		pluginLoadingInProgress = true
		plugin = this
		
		PikoPluginLibApi.init()
		
		super.onLoad()
	}
	
	override fun onStart() {
		server.pluginManager.registerEvents(MenuListener(), this)
		server.pluginManager.registerEvents(PluginListener(), this)
		server.pluginManager.registerEvents(PlayerListener(), this)
		
//		api.commands.getOrCreate("piko").addCommand(ReloadSubCommand())
		
//		TestFunctions.eventFunctionTest(this)
	}
	
}