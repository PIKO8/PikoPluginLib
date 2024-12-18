package ru.piko.pikopluginlib.Commands

import org.bukkit.command.CommandSender

abstract class HelperSubCommand : AbstractCommand() {
	
	abstract val description: String
	abstract val syntax: String
	
}

class SimpleHelperSubCommand(
	override val name: String,
	override val description: String,
	override val syntax: String,
	private val action: (HelperSubCommand, CommandSender, Array<String>) -> Unit,
	private val tabs: (HelperSubCommand, CommandSender, Array<String>) -> List<String>? = { _, _, _ -> listOf("") },
	private val permissions: ((HelperSubCommand, CommandSender, Array<String>) -> List<String>?)? = null,
) : HelperSubCommand() {
	
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