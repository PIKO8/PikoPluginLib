package ru.piko.pikopluginlib.Files.Abstract.folder

import ru.piko.pikopluginlib.Api.PikoPlugin

abstract class AbstractPikoPluginFolder<P: PikoPlugin<P>>(plugin: P) : AbstractPluginFolder<P>(plugin) {
	
	init {
//		plugin.folder = this
	}
	
}