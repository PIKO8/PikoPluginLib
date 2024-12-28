package ru.piko.pikopluginlib.Files.Abstract.file

import ru.piko.pikopluginlib.Files.Abstract.AbstractResource
import ru.piko.pikopluginlib.Files.Abstract.folder.AbstractFolder
import ru.piko.pikopluginlib.Files.FileExtension
import java.io.File

typealias AbstractFileAny = AbstractFile<*>

abstract class AbstractFile<H: AbstractFileHandler>(
	name: String,
	val parent: AbstractFolder,
	val extension: FileExtension<H>
) : AbstractResource(name) {
	
	init {
		file // создание файла и папок
	}
	
	val file: File
		get() {
			val abstractFile = abstractFile
			if (!abstractFile.exists()) {
				abstractFile.createNewFile()
			}
			return abstractFile
		}
	
	protected open val abstractFile: File = extension.toFile(parent, name)
	
	open val handler: H
		get() = extension.handler
	
}