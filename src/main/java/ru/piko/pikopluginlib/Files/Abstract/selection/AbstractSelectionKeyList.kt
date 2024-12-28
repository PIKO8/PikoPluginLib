package ru.piko.pikopluginlib.Files.Abstract.selection

import ru.piko.pikopluginlib.Files.Interfaces.InterfaceSelection

abstract class AbstractSelectionKeyList<S: AbstractSelectionValue<*>>(
	key: String,
	//val list: MutableList<S> = mutableListOf()
	val list: AbstractSelectionList<S>
) : AbstractSelectionKeyValueArgs<MutableList<S>>(key, list), InterfaceSelection<S> {
	
	override fun addResource(resource: S): Boolean {
		return list.addResource(resource)
	}
	
	operator fun get(index: Int): S {
		return list[index]
	}
	
	fun remove(resource: S): Boolean {
		return list.remove(resource)
	}
	
	fun removeAt(index: Int): S {
		return list.removeAt(index)
	}
	
	val size: Int get() = list.size
	
}

