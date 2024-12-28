package ru.piko.pikopluginlib.Files.Abstract.selection

abstract class AbstractSelectionValue<V: Any> : AbstractSelection("") {
	
	abstract val value: V?
	
	fun contains(): Boolean {
		return value != null
	}
	
}