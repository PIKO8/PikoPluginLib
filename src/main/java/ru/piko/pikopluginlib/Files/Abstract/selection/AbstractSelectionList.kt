package ru.piko.pikopluginlib.Files.Abstract.selection

import ru.piko.pikopluginlib.Files.Interfaces.InterfaceSelection

abstract class AbstractSelectionList<S: AbstractSelectionValue<*>>
: AbstractSelectionValue<MutableList<S>>(), InterfaceSelection<S> {
	
	val list: MutableList<S> get() = value ?: error("There is no given element. Use contains()")
	
	override fun addResource(resource: S): Boolean {
		list.add(resource)
		return add(resource)
	}
	
	override fun removeResource(resource: S): Boolean {
		list.remove(resource)
		return remove(resource)
	}
	
	fun removeResourceAt(index: Int): S {
		list.removeAt(index)
		return removeAt(index)
	}
	
	abstract fun add(resource: S): Boolean
	
	abstract operator fun get(index: Int): S
	
	abstract fun remove(resource: S): Boolean
	
	abstract fun removeAt(index: Int): S
	
	abstract val size: Int
	
}