package ru.piko.pikopluginlib.Files.Abstract.folder

import org.bukkit.plugin.java.JavaPlugin
import java.nio.file.Path

abstract class AbstractPluginFolder<P: JavaPlugin>(val plugin: P) : AbstractFolder(plugin.name) {
	
	override val abstractPath: Path
		get() = plugin.dataFolder.toPath()
	
}

