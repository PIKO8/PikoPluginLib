package ru.piko.pikopluginlib.Commands.SubCommands

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import ru.piko.pikopluginlib.Commands.SubCommand
import ru.piko.pikopluginlib.Main
import ru.piko.pikopluginlib.PikoPlugin

class ReloadSubCommand : SubCommand() {
	override val name: String = "reload"
	
	val description: String = "Восстанавливает ссылки плагинов"
	
	val syntax: String = "/piko reload"
	
	override fun getPermissions(sender: CommandSender, args: Array<String>): List<String> {
		return listOf("piko.commands.admin.reload")
	}
	
	override fun perform(sender: CommandSender, args: Array<String>) {
		val main = Main.getPlugin()
		for (plugin in Bukkit.getServer().pluginManager.plugins) {
			if (plugin != main && plugin is JavaPlugin && plugin is PikoPlugin) {
				// found a PikoPlugin instance, do something with it
				plugin.registerPikoLib()
			}
		}
	}
	
	override fun arguments(sender: CommandSender, args: Array<String>): List<String> = listOf("")
}