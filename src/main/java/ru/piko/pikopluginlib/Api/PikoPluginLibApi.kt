package ru.piko.pikopluginlib.Api

import ru.piko.pikopluginlib.Api.Managers.CommandsManagerApi
import ru.piko.pikopluginlib.Api.Managers.PikoPluginsManagerApi
import ru.piko.pikopluginlib.Api.Managers.PlayerDataManagerApi

object PikoPluginLibApi {
	
	var isInit = false
		internal set
	
	lateinit var plugins: PikoPluginsManagerApi
	
	lateinit var playerData: PlayerDataManagerApi
	
	lateinit var commands: CommandsManagerApi
	
	internal fun init() {
		isInit = true
		
		plugins = PikoPluginsManagerApi()
		
		playerData = PlayerDataManagerApi()
		
		commands = CommandsManagerApi()
	}
	
}