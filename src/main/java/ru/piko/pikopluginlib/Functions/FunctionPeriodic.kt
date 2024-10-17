package ru.piko.pikopluginlib.Functions

import org.bukkit.plugin.java.JavaPlugin
import ru.piko.pikopluginlib.Utils.NotRecommended

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
) : FunctionAbstract(plugin, ticks, delay, id, stopAllWithId) {
	/**
	 * EN: If the function returns false, then end  <br>
	 * RU: Если функция вернёт false то завершаем
	 */
	override fun run() {
		if (!function.invoke()) {
			destroySelf()
		}
	}
	
	override fun destroySelf() {
		task?.cancel()
		list.remove(this)
	}
	
	override fun init() {
		list.add(this)
	}
	
	
	companion object {
		val list: MutableList<FunctionPeriodic> = ArrayList()
		
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
		
		fun destroy(periodic: FunctionPeriodic) {
			periodic.destroySelf()
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