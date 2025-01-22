package ru.piko.pikopluginlib.Functions

import org.bukkit.plugin.java.JavaPlugin
import ru.piko.pikopluginlib.Utils.PikoAnnotation.NotRecommended
import java.util.*

/**
 * Работает раз в ticks, но ограниченное количество раз(maxRepeats)
 */
class FunctionRepeater private constructor(
    plugin: JavaPlugin,
    ticks: Long,
    val maxRepeats: Int,
    delay: Long = 0,
    id: String = "",
    stopAllWithId: Boolean,
    val function: (Int) -> Unit,
) : FunctionAbstractBukkitScheduler(plugin, ticks, delay, id, stopAllWithId) {
	private var repeatCount = 0
	
	override fun run() {
		try {
			function.invoke(repeatCount)
		} catch (e: Exception) {
			plugin.logger.warning("[PikoPluginLib] (${this::class.java}) Перехвачена ошибка в функции с id='$id'. Ошибка:")
			e.printStackTrace()
		}
		repeatCount++
		if (repeatCount >= maxRepeats) {
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
	
	companion object : ICompanionFunction<FunctionRepeater> {
		override val list: MutableList<FunctionRepeater> = Collections.synchronizedList(mutableListOf())
		
		fun create(
        plugin: JavaPlugin,
        ticks: Long,
        maxRepeats: Int,
        delay: Long = 0,
        id: String = "",
        stopAllWithId: Boolean = false,
        function: (Int) -> Unit,
    ): FunctionRepeater {
			if (stopAllWithId) {
				destroyAll(plugin, id)
			}
			
			val functionRepeater = FunctionRepeater(plugin, ticks, maxRepeats, delay, id, stopAllWithId, function)
			functionRepeater.initFunction()
			return functionRepeater
		}
	}
}