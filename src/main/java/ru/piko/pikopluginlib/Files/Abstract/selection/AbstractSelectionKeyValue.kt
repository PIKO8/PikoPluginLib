package ru.piko.pikopluginlib.Files.Abstract.selection

abstract class AbstractSelectionKeyValue<V: Any>(key: String) : AbstractSelection(key) {
	
	abstract val value: AbstractSelectionValue<V>
	
}
