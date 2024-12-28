package ru.piko.pikopluginlib.Files.Abstract.folder

import ru.piko.pikopluginlib.Functions.FunctionTimer
import ru.piko.pikopluginlib.Utils.Extends.FilesExtends.resolveIfDirectory
import ru.piko.pikopluginlib.Utils.InternalObject.nextTact
import java.nio.file.Path

abstract class AbstractParentFolder(name: String, val parent: AbstractFolder) : AbstractFolder(name) {

	init {
		FunctionTimer.nextTact { parent.addResource(this) }
	}
	
	override val abstractPath: Path
		get() = parent.path.resolveIfDirectory(name)
	
}