package ru.piko.pikopluginlib.Api

import org.bukkit.Bukkit
import ru.piko.pikopluginlib.Commands.AbstractHelper
import ru.piko.pikopluginlib.Commands.CommandManager
import ru.piko.pikopluginlib.Commands.DefaultHelper
import ru.piko.pikopluginlib.PikoPlugin

class CommandsManagerApi {
	
	private val commandManagerMap = mutableMapOf<String, CommandManager>()
	
	/**
	 * Creates a CommandManager for handling commands related to this plugin.
	 *
	 * @param mainCommand The main command associated with the CommandManager.
	 * @return A new instance of CommandManager.
	 * @throws IllegalArgumentException if the command is not registered.
	 */
	fun create(mainCommand: String, helper: AbstractHelper? = DefaultHelper()): CommandManager {
		val command = Bukkit.getServer().getPluginCommand(mainCommand)
		if (command != null) {
			val plugin = command.plugin
			
			if (plugin !is PikoPlugin) throw IllegalArgumentException("Command '$mainCommand' is registered not in PikoPlugin")
			
			val commandManager = CommandManager(plugin.id, mainCommand, helper)
			
			command.setExecutor(commandManager)
			commandManagerMap[mainCommand] = commandManager
			return commandManager
		} else {
			throw IllegalArgumentException("Command '$mainCommand' is not registered.")
		}
	}
	
	fun getOrCreate(mainCommand: String, helper: AbstractHelper? = DefaultHelper()): CommandManager {
		if (has(mainCommand)) {
			return get(mainCommand)!!
		}
		return create(mainCommand, helper)
	}
	
	fun get(mainCommand: String): CommandManager? {
		if (commandManagerMap.containsKey(mainCommand)) {
			return commandManagerMap[mainCommand]
		}
		return null
	}
	
	fun has(mainCommand: String): Boolean {
		return commandManagerMap.containsKey(mainCommand)
	}
	
	fun add(mainCommand: String, manager: CommandManager) {
		if (!commandManagerMap.containsKey(mainCommand)) {
			commandManagerMap[mainCommand] = manager
		} else {
			commandManagerMap.replace(mainCommand, manager)
		}
	}
	
}
