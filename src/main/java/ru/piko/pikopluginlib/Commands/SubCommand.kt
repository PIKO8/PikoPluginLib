package ru.piko.pikopluginlib.Commands

import org.bukkit.command.CommandSender

abstract class SubCommand : AbstractCommand()

class SimpleSubCommand(
	override val name: String,
	private val action: (SubCommand, CommandSender, Array<String>) -> Unit,
	private val tabs: (SubCommand, CommandSender, Array<String>) -> List<String>? = { _, _, _ -> listOf("") },
	private val permissions: ((SubCommand, CommandSender, Array<String>) -> List<String>?)? = null,
) : SubCommand() {
	
	override fun perform(sender: CommandSender, args: Array<String>) {
		action(this, sender, args)
	}
	
	override fun arguments(sender: CommandSender, args: Array<String>): List<String> {
		return tabs(this, sender, args) ?: listOf("")
	}
	
	override fun getPermissions(sender: CommandSender, args: Array<String>): List<String>? {
		return permissions?.invoke(this, sender, args)
	}
}