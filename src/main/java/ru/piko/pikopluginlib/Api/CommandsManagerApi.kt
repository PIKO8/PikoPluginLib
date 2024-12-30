package ru.piko.pikopluginlib.Api

import org.bukkit.Bukkit
import ru.piko.pikopluginlib.Commands.AbstractHelper
import ru.piko.pikopluginlib.Commands.CommandManager
import ru.piko.pikopluginlib.Commands.DefaultHelper

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
		val command = Bukkit.getServer().getPluginCommand(mainCommand) ?: throw IllegalArgumentException(
			"Failed to create command manager: command '$mainCommand' is not registered in server commands."
		)
		val plugin = command.plugin
		
		if (plugin !is PikoPlugin) throw IllegalArgumentException(
			"Failed to create command manager: command '$mainCommand' is registered in a plugin that is not a PikoPlugin."
		)
		
		val commandManager = CommandManager(plugin.id, mainCommand, helper)
		
		command.setExecutor(commandManager)
		commandManagerMap[mainCommand] = commandManager
		return commandManager
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
	
	fun remove(mainCommand: String) {
		val manager = commandManagerMap[mainCommand] ?: return
		
		val command = Bukkit.getServer().getPluginCommand(mainCommand) ?: throw IllegalArgumentException(
			"Failed to remove command manager: command '$mainCommand' is not registered in server commands."
		)
		
		val plugin = command.plugin
		if (plugin !is PikoPlugin) throw IllegalArgumentException(
			"Failed to remove command manager: command '$mainCommand' is registered in a plugin that is not a PikoPlugin."
		)
		
		manager.clearCommands()
		command.unregister(Bukkit.getServer().commandMap)
		
		commandManagerMap.remove(mainCommand)
	}
	
}
