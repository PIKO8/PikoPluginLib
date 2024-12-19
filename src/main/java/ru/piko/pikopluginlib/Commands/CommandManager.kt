package ru.piko.pikopluginlib.Commands

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import ru.piko.pikopluginlib.Utils.UText.color

class CommandManager(
	namePikoPlugin: String,
	commandName: String,
	helper: AbstractHelper? = null,
) : AbstractCommandManager(namePikoPlugin, commandName, helper), TabExecutor {
	
	override fun onCommand(
		commandSender: CommandSender,
		command: Command,
		s: String,
		strings: Array<String>
	): Boolean {
		if (strings.isEmpty()) {
			helper?.page(commandSender,1)
			return true
		}
		
		commands.find { it.name.equals(strings[0], ignoreCase = true) }?.let { subCommand ->
			if (!subCommand.hasPermission(commandSender, strings)) {
				commandSender.sendMessage(color("Недостаточно прав!"))
				return true
			}
			subCommand.perform(commandSender, strings)
		}
		
		return true
	}
	
	override fun onTabComplete(
		commandSender: CommandSender,
		command: Command,
		s: String,
		strings: Array<String>
	): List<String> {
		return when {
			strings.size == 1 -> {
				commands
					.filter { it.hasPermission(commandSender, strings) }
					.map { it.name }
					.filter { it.lowercase().contains(strings[0].lowercase()) }
			}
			strings.size >= 2 -> {
				commands
					.find { it.name.equals(strings[0], ignoreCase = true) }
					?.arguments(commandSender, strings)
					?: listOf("Неверные_предыдущие_значения!")
			}
			else -> listOf("Неверные_предыдущие_значения!")
		}
	}
}