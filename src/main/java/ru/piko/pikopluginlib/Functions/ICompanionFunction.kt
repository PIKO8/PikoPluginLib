package ru.piko.pikopluginlib.Functions

import org.bukkit.plugin.java.JavaPlugin
import ru.piko.pikopluginlib.Utils.PikoAnnotation.NotRecommended

interface ICompanionFunction<F : FunctionAbstract> {
	
	val list: MutableList<F>
	
	fun destroy(function: F) {
		function.destroySelf()
	}
	
	@NotRecommended("Может сломать что-нибудь в других плагинах лучше использовать destroyAll(plugin: JavaPlugin, id: String)")
	@Deprecated("Не рекомендованный")
	fun destroyAll(id: String) {
		FunctionAbstract.destroyAll(list, id)
	}
	
	fun destroyAll(plugin: JavaPlugin, id: String) {
		FunctionAbstract.destroyAll(list, plugin, id)
	}
	
}