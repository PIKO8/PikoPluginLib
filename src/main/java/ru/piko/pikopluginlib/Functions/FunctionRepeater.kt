package ru.piko.pikopluginlib.Functions

import org.bukkit.plugin.java.JavaPlugin
import ru.piko.pikopluginlib.Utils.NotRecommended
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
    val function: () -> Unit,
) : FunctionAbstract(plugin, ticks, delay, id, stopAllWithId) {
	private var repeatCount = 0
	
	override fun run() {
		function.invoke()
		repeatCount++
		if (repeatCount >= maxRepeats) {
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
		val list: MutableList<FunctionRepeater> = Collections.synchronizedList(mutableListOf())
		
		fun create(
        plugin: JavaPlugin,
        ticks: Long,
        maxRepeats: Int,
        delay: Long = 0,
        id: String = "",
        stopAllWithId: Boolean = false,
        function: () -> Unit,
    ): FunctionRepeater {
			if (stopAllWithId) {
				destroyAll(plugin, id)
			}
			
			val functionRepeater = FunctionRepeater(plugin, ticks, maxRepeats, delay, id, stopAllWithId, function)
			functionRepeater.initFunction()
			return functionRepeater
		}
		
		fun destroy(repeater: FunctionRepeater) {
			repeater.destroySelf()
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