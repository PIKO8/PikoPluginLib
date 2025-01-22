package ru.piko.pikopluginlib.Functions

import org.bukkit.plugin.java.JavaPlugin
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
) : FunctionAbstractBukkitScheduler(plugin, ticks, delay, id, stopAllWithId) {
	
	
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
	
	companion object : ICompanionFunction<FunctionUnit> {
		override val list: MutableList<FunctionUnit> = Collections.synchronizedList(mutableListOf())
		
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
		
	}
}