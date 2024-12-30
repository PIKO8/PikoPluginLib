package ru.piko.pikopluginlib.Api

object PikoPluginLibApi {
	
	internal var init = false
	val isInit get() = init
	
	lateinit var plugins: PikoPluginsManagerApi
	
	lateinit var playerData: PlayerDataManagerApi
	
	lateinit var commands: CommandsManagerApi
	
	internal fun init() {
		init = true
		
		plugins = PikoPluginsManagerApi()
		
		playerData = PlayerDataManagerApi()
		
		commands = CommandsManagerApi()
	}
	
	
	
}