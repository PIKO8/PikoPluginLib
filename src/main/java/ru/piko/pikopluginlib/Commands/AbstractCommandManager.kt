package ru.piko.pikopluginlib.Commands

import org.bukkit.command.TabExecutor
import ru.piko.pikopluginlib.PikoPlugin
import ru.piko.pikopluginlib.PikoPluginData
import ru.piko.pikopluginlib.Utils.main

abstract class AbstractCommandManager(
	val namePikoPlugin: String,
	val commandName: String,
	_helper: AbstractHelper? = null
) {
	
	var helper: AbstractHelper? = _helper
		set(value) {
			commands.remove(field as? AbstractCommand)
			field = value
			if (value != null) {
				commands.add(value as AbstractCommand)
			}
		}
	
	val commands = mutableListOf<AbstractCommand>()
	
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