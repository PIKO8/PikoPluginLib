package ru.piko.pikopluginlib.Files.Release.Yaml

import ru.piko.pikopluginlib.Files.Abstract.file.AbstractFileAny
import ru.piko.pikopluginlib.Files.Abstract.selection.AbstractSelection
import ru.piko.pikopluginlib.Files.Abstract.selection.AbstractSelectionKeyValue
import ru.piko.pikopluginlib.Functions.FunctionTimer
import ru.piko.pikopluginlib.Utils.InternalObject.nextTact

//class YamlKeyValue<V: Any>(
//	val parent: AbstractFileAny,
//	val key: String,
//	override val value: V
//) : AbstractSelectionKeyValue<YamlKeyValue<V>>(key) {
//
//	init {
//		FunctionTimer.nextTact { parent.addResource(this) }
//	}
//
//	override fun load(reload: Boolean) {
//		return Unit
//	}
//
//	override fun save() {
//		return Unit
//	}
//
//}