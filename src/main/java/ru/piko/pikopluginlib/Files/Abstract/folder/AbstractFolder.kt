package ru.piko.pikopluginlib.Files.Abstract.folder

import ru.piko.pikopluginlib.Files.Abstract.AbstractResource
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.exists

abstract class AbstractFolder(name: String) : AbstractResource(name) {
	
	protected val objects: MutableMap<String, AbstractResource> = mutableMapOf()
	
	fun addResource(resource: AbstractResource) {
		objects[resource.name] = resource
	}
	
	public val path: Path
		get() {
			if (!abstractPath.exists()) {
				abstractPath.createDirectories()
			}
			return abstractPath
		}
	
	protected abstract val abstractPath: Path
	
	override fun load(reload: Boolean) {
		objects.values.forEach { it.load(reload) }
	}
	
	override fun save() {
		objects.values.forEach { it.save() }
	}
	
}