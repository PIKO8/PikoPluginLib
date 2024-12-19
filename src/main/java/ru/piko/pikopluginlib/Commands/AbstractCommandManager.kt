package ru.piko.pikopluginlib.Commands

import org.bukkit.command.TabExecutor
import ru.piko.pikopluginlib.PikoPlugin
import ru.piko.pikopluginlib.PikoPluginData
import ru.piko.pikopluginlib.Utils.PikoVariables.main

abstract class AbstractCommandManager(
	val namePikoPlugin: String,
	val commandName: String,
	_helper: AbstractHelper? = null
) {
	
	val commands: MutableList<AbstractCommand>
	
	init {
		commands = mutableListOf()
		
		_helper?.let { addCommand(it) }
	}
	
	@Suppress("unused")
	var helper: AbstractHelper? = _helper
		set(value) {
			commands.remove(field as? AbstractCommand)
			field = value
			if (value != null) {
				addCommand(value)
			}
		}
	
	val pluginData: PikoPluginData?
		get() = main.getPikoPluginData(namePikoPlugin)
	
	val plugin: PikoPlugin?
		get() = pluginData?.plugin
	
	fun addCommand(command: AbstractCommand) {
		if (!commands.contains(command)) {
			command.commandManager = this
			commands.add(command)
		}
	}
	
	fun clearCommands() {
		commands.clear()
	}
}