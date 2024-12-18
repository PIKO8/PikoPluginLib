package ru.piko.pikopluginlib.Functions

import org.bukkit.plugin.java.JavaPlugin
import ru.piko.pikopluginlib.Utils.NotRecommended
import java.util.*

/**
 * Сработает один раз через delay
 */
class FunctionTimer private constructor(
    plugin: JavaPlugin,
    delay: Long,
    id: String,
    stopAllWithId: Boolean,
    val function: () -> Unit,
) : FunctionAbstract(plugin, 1, delay, id, stopAllWithId) {
	override fun run() {
		try {
			function.invoke()
		} catch (e: Exception) {
			plugin.logger.warning("[PikoPluginLib] (${this::class.java}) Перехвачена ошибка в функции с id='$id'. Ошибка:")
			e.printStackTrace()
		} finally {
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
	
	
	companion object {
		val list: MutableList<FunctionTimer> = Collections.synchronizedList(mutableListOf())
		
		fun create(
        plugin: JavaPlugin,
        delay: Long,
        id: String = "",
        stopAllWithId: Boolean = false,
        function: () -> Unit,
    ): FunctionTimer {
			if (stopAllWithId) {
				destroyAll(plugin, id)
			}
			
			val functionTimer = FunctionTimer(plugin, delay, id, stopAllWithId, function)
			functionTimer.initFunction()
			return functionTimer
		}
		
		fun destroy(timer: FunctionTimer) {
			timer.destroySelf()
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