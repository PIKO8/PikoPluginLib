package ru.piko.pikopluginlib.Files.Abstract.folder

import ru.piko.pikopluginlib.PikoPlugin

abstract class AbstractPikoPluginFolder<P: PikoPlugin>(plugin: P) : AbstractPluginFolder<P>(plugin) {
	
	init {
		plugin.folder = this
	}
	
}