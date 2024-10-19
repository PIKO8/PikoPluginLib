package ru.piko.pikopluginlib.Functions

import org.bukkit.plugin.java.JavaPlugin
import ru.piko.pikopluginlib.Utils.NotRecommended

/**
 * Будет работать пока её не остановят. Сама не остановиться ни когда
 */
class FunctionUnit private constructor(
	plugin: JavaPlugin,
	ticks: Long,
	delay: Long = 0,
	id: String = "",
	stopAllWithId: Boolean,
	val function: () -> Unit,
) : FunctionAbstract(plugin, ticks, delay, id, stopAllWithId) {
	
	
	override fun run() {
		function.invoke()
	}
	
	override fun destroySelf() {
		task?.cancel()
		list.remove(this)
	}
	
	override fun init() {
		list.add(this)
	}
	
	companion object {
		val list: MutableList<FunctionUnit> = ArrayList()
		
		fun create(
			plugin: JavaPlugin,
			ticks: Long,
			delay: Long = 0,
			id: String = "",
			stopAllWithId: Boolean = false,
			function: () -> Unit,
		): FunctionUnit {
			if (stopAllWithId) {
				destroyAll(plugin, id)
			}
			
			val functionUnit = FunctionUnit(plugin, ticks, delay, id, stopAllWithId, function)
			functionUnit.initFunction()
			return functionUnit
		}
		
		fun destroy(unit: FunctionUnit) {
			unit.destroySelf()
		}
		
		@NotRecommended("Может сломать что-нибудь в других плагинах лучше использовать destroyAll(plugin: JavaPlugin, id: String)")
		@Deprecated("Не рекомендованный")
		fun destroyAll(id: String) {
			list.forEach {
				if (it.id == id) {
					it.destroySelf()
				}
			}
		}
		
		fun destroyAll(plugin: JavaPlugin, id: String) {
			list.forEach {
				if (it.plugin == plugin && it.id == id) {
					it.destroySelf()
				}
			}
		}
	}
}