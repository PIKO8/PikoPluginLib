package ru.piko.pikopluginlib.Files.Release.Yaml

import org.bukkit.configuration.file.FileConfiguration
import ru.piko.pikopluginlib.Files.Abstract.file.AbstractFile
import ru.piko.pikopluginlib.Files.Abstract.folder.AbstractFolder
import ru.piko.pikopluginlib.Files.FileExtension

abstract class YamlFile(
	name: String,
	parent: AbstractFolder
) : AbstractFile<YamlFileHandler>(name, parent, FileExtension.YAML) {
	
	private lateinit var conf: FileConfiguration
	
	override fun load(reload: Boolean) {
		conf = handler.configuration(this)
		conf.load(this.file)
		//onLoad(conf)
	}
	
	override fun save() {
		conf = handler.configuration(this)
		conf.save(this.file)
		//onSave(conf)
	}
	
//	abstract fun onLoad(conf: FileConfiguration)
//	abstract fun onSave(conf: FileConfiguration)
}