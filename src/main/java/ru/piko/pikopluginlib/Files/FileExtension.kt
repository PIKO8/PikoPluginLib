package ru.piko.pikopluginlib.Files

import ru.piko.pikopluginlib.Files.Abstract.file.AbstractFileHandler
import ru.piko.pikopluginlib.Files.Abstract.folder.AbstractFolder
import ru.piko.pikopluginlib.Files.Release.Yaml.YamlFileHandler
import ru.piko.pikopluginlib.Utils.Extends.FilesExtends.resolveIfDirectory
import java.io.File

class FileExtension<H: AbstractFileHandler>(
	val extension: String,
	val handler: H
) {
	
	fun toFile(parent: AbstractFolder, name: String): File {
		 return parent.path.resolveIfDirectory("$name.$extension").toFile()
	}
	
	companion object {
		
		val YAML = FileExtension("yml", YamlFileHandler())
		
		
		
		
	}
	
}