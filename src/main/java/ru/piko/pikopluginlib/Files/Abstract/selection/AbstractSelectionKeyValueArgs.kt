package ru.piko.pikopluginlib.Files.Abstract.selection

abstract class AbstractSelectionKeyValueArgs<V: Any>(key: String, value_: AbstractSelectionValue<V>) : AbstractSelectionKeyValue<V>(key) {
	
	override val value: AbstractSelectionValue<V> = value_
	
}