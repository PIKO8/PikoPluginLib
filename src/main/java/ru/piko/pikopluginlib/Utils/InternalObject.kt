package ru.piko.pikopluginlib.Utils

import ru.piko.pikopluginlib.Functions.FunctionTimer
import ru.piko.pikopluginlib.Functions.FunctionTimer.Companion.create
import ru.piko.pikopluginlib.Main

internal object InternalObject {
	val main: Main = Main.getPlugin() ?: throw IllegalStateException("PikoPluginLib Not uploaded yet!!!")
	
	fun FunctionTimer.Companion.nextTact(function: () -> Unit) {
		create(main, 0, function = function)
	}
}