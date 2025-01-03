package ru.piko.pikopluginlib.Functions

import org.bukkit.plugin.java.JavaPlugin
import ru.piko.pikopluginlib.Utils.PikoAnnotation.NotRecommended
import java.util.*

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
		try {
			function.invoke()
		} catch (e: Exception) {
			plugin.logger.warning("[PikoPluginLib] (${this::class.java}) Перехвачена ошибка в функции с id='$id'. Ошибка:")
			e.printStackTrace()
		}
	}
	
	override fun destroySelf() {
		task?.let { if (!it.isCancelled) it.cancel() }
		FunctionAbstract.removeObjectInList(list, this)
	}
	
	override fun init() {
		list.add(this)
	}
	
	companion object {
		val list: MutableList<FunctionUnit> = Collections.synchronizedList(mutableListOf())
		
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
			FunctionAbstract.destroyAll(list, id)
		}
		
		fun destroyAll(plugin: JavaPlugin, id: String) {
			FunctionAbstract.destroyAll(list, plugin, id)
		}
	}
}