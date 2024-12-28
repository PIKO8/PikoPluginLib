package ru.piko.pikopluginlib.Utils.Extends

import ru.piko.pikopluginlib.Utils.Exceptions.DirectoryResolutionException
import java.nio.file.Files
import java.nio.file.Path

object FilesExtends {
	
	fun Path.resolveTryIfDirectory(additionalPath: String): Path? {
		return if (Files.isDirectory(this)) {
			resolve(additionalPath)
		} else {
			null
		}
	}
	
	fun Path.resolveIfDirectory(additionalPath: String): Path {
		return if (Files.isDirectory(this)) {
			resolve(additionalPath)
		} else {
			throw DirectoryResolutionException.standard(additionalPath,this)
		}
	}
	
	
}