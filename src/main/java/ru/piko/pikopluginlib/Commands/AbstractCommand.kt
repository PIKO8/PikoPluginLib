package ru.piko.pikopluginlib.Commands

import org.bukkit.command.CommandSender
import java.util.Objects

abstract class AbstractCommand {
	lateinit var commandManager: AbstractCommandManager
	
	abstract val name: String
	
	abstract fun perform(sender: CommandSender, args: Array<String>)
	abstract fun arguments(sender: CommandSender, args: Array<String>): List<String>
	
	open fun getPermissions(sender: CommandSender, args: Array<String>): List<String>? = null
	open fun hasPermission(sender: CommandSender, args: Array<String>): Boolean {
		val permissions = getPermissions(sender, args)
		return permissions.isNullOrEmpty() || permissions.all { sender.hasPermission(it) }
	}
	
	open override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false
		
		other as AbstractCommand
		return Objects.equals(name, other.name)
	}
	
	open override fun hashCode(): Int {
		return Objects.hash(name)
	}
}