package ru.piko.pikopluginlib.Utils

import ru.piko.pikopluginlib.Main

object PikoVariables {
	val main: Main = Main.getPlugin() ?: throw IllegalStateException("PikoPluginLib Not uploaded yet!!!")
}