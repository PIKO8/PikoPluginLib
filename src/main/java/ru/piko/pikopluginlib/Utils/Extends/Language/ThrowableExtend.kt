package ru.piko.pikopluginlib.Utils.Extends.Language

import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Level

object ThrowableExtend {
	
	fun Throwable.bukkitLog(plugin: JavaPlugin, level: Level, message: String) {
		plugin.logger.log(level, message, this)
	}
	
	fun Throwable.bukkitWarning(plugin: JavaPlugin, message: String) {
		bukkitLog(plugin, Level.WARNING, message)
	}
	
	fun Throwable.bukkitSevere(plugin: JavaPlugin, message: String) {
		bukkitLog(plugin, Level.SEVERE, message)
	}
	
}