package ru.piko.pikopluginlib.Functions

import org.bukkit.plugin.java.JavaPlugin
import ru.piko.pikopluginlib.Utils.PikoAnnotation.NotRecommended
import java.util.*

/**
 * Пока передаётся true будет работать раз в ticks
 */
class FunctionPeriodic private constructor(
    plugin: JavaPlugin,
    ticks: Long,
    delay: Long = 0,
    id: String = "",
    stopAllWithId: Boolean,
    val function: () -> Boolean,
) : FunctionAbstractBukkitScheduler(plugin, ticks, delay, id, stopAllWithId) {
	/**
	 * EN: If the function returns false, then end  <br>
	 * RU: Если функция вернёт false то завершаем
	 */
	override fun run() {
		val result: Boolean = try {
			function.invoke()
		} catch (e: Exception) {
			plugin.logger.warning("[PikoPluginLib] (${this::class.java}) Перехвачена ошибка в функции с id='$id'. Ошибка:")
			e.printStackTrace()
			false
		}
		if (!result) {
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
	
	
	companion object : ICompanionFunction<FunctionPeriodic> {
		override val list: MutableList<FunctionPeriodic> = Collections.synchronizedList(mutableListOf())
		
		fun create(
        plugin: JavaPlugin,
        ticks: Long,
        delay: Long = 0,
        id: String = "",
        stopAllWithId: Boolean = false,
        function: () -> Boolean,
    ): FunctionPeriodic {
			if (stopAllWithId) {
				destroyAll(plugin, id)
			}
			
			val functionPeriodic = FunctionPeriodic(plugin, ticks, delay, id, stopAllWithId, function)
			functionPeriodic.initFunction()
			return functionPeriodic
		}
	}
}