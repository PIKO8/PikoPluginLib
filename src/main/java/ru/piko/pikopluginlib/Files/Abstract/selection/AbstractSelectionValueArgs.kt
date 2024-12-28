package ru.piko.pikopluginlib.Files.Abstract.selection

abstract class AbstractSelectionValueArgs<V: Any>(value_: V?) : AbstractSelectionValue<V>() {
	
	override val value: V? = value_
	
}