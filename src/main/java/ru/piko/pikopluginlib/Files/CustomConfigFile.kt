package ru.piko.pikopluginlib.Files

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

abstract class CustomConfigFile(dataFolder: File, relativePath: String) {
	protected var file: File = File(dataFolder, "$relativePath.yml")
	protected lateinit var conf: FileConfiguration
	
	fun init() {
		if (!file.parentFile.exists()) {
			file.parentFile.mkdirs()
		}
		if (!file.exists()) {
			try {
				file.createNewFile()
				onCreate()
			} catch (e: Exception) {
				e.printStackTrace()
			}
		}
		conf = YamlConfiguration.loadConfiguration(file)
		onLoad(false)
	}
	
	fun save() {
		try {
			conf.save(file)
			onSave();
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}
	
	fun reload() {
		conf = YamlConfiguration.loadConfiguration(file)
		onLoad(true)
	}
	
	open fun onSave() {}
	
	open fun onCreate() {}
	
	open fun onLoad(reload: Boolean) {}
	
}