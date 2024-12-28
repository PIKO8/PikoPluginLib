package ru.piko.pikopluginlib.Files.Release.Yaml

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import ru.piko.pikopluginlib.Files.Abstract.file.AbstractFile
import ru.piko.pikopluginlib.Files.Abstract.file.AbstractFileHandler

class YamlFileHandler : AbstractFileHandler() {

	fun configuration(abstractFile: AbstractFile<*>): FileConfiguration {
		return YamlConfiguration.loadConfiguration(abstractFile.file)
	}

}