package ru.piko.pikopluginlib.Functions

import org.bukkit.plugin.java.JavaPlugin
import ru.piko.pikopluginlib.Utils.PikoAnnotation.NotRecommended
import java.util.*

/**
 * Сработает один раз когда выполнено condition и выполнит function
 */
class FunctionConditional private constructor(
    plugin: JavaPlugin,
    ticks: Long,
    delay: Long = 0,
    id: String = "",
    stopAllWithId: Boolean,
    val condition: () -> Boolean,
    val function: () -> Unit,
) : FunctionAbstractBukkitScheduler(plugin, ticks, delay, id, stopAllWithId) {
	override fun run() {
		val result: Boolean = try {
			condition.invoke()
		} catch (e: Exception) {
			plugin.logger.warning("[PikoPluginLib] (${this::class.java}) Перехвачена ошибка в функции(условие) с id='$id'. Ошибка:")
			e.printStackTrace()
			false
		}
		if (result) {
			try {
				function.invoke()
			} catch (e: Exception) {
				plugin.logger.warning("[PikoPluginLib] (${this::class.java}) Перехвачена ошибка в функции(основной) с id='$id'. Ошибка:")
				e.printStackTrace()
			}
			destroySelf()
		}
	}
	
	override fun destroySelf() {
		task?.let { if (!it.isCancelled) it.cancel() }
		FunctionAbstract.removeObjectInList(list, this)
	}
	
	override fun init() {
		list.add(this)
	}
	
	companion object : ICompanionFunction<FunctionConditional> {
		override val list: MutableList<FunctionConditional> = Collections.synchronizedList(mutableListOf())
		
		fun create(
        plugin: JavaPlugin,
        ticks: Long,
        delay: Long = 0,
        id: String = "",
        stopAllWithId: Boolean = false,
        condition: () -> Boolean,
        function: () -> Unit,
    ): FunctionConditional {
			if (stopAllWithId) {
				destroyAll(plugin, id)
			}
			
			val functionConditional = FunctionConditional(plugin, ticks, delay, id, stopAllWithId, condition, function)
			functionConditional.initFunction()
			return functionConditional
		}
	}
}