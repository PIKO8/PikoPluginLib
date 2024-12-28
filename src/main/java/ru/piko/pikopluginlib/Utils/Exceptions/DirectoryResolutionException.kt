package ru.piko.pikopluginlib.Utils.Exceptions

import ru.piko.pikopluginlib.Files.Abstract.folder.AbstractFolder
import java.nio.file.Path

class DirectoryResolutionException(message: String) : IllegalArgumentException(message) {
	
	companion object {
		
		fun standard(name: String, parent: AbstractFolder): DirectoryResolutionException {
			return standard(name, parent.path)
		}
		
		fun standard(name: String, path: Path): DirectoryResolutionException {
			return DirectoryResolutionException(
				"Cannot resolve directory path for '$name': parent path '${path}' is not a valid directory"
			)
		}
		
	}
	
}