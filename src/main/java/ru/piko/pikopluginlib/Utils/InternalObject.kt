package ru.piko.pikopluginlib.Utils

import ru.piko.pikopluginlib.Api.PikoPluginLibApi
import ru.piko.pikopluginlib.Functions.FunctionTimer
import ru.piko.pikopluginlib.Main

internal object InternalObject {
	val main: Main = Main.getPlugin() ?: throw IllegalStateException("PikoPluginLib Not uploaded yet!!!")
	
	fun FunctionTimer.Companion.nextTick(function: () -> Unit) {
		create(main, 0, function = function)
	}
}