package ru.piko.pikopluginlib.Functions

import org.bukkit.plugin.java.JavaPlugin
import ru.piko.pikopluginlib.Functions.Builder.BuilderResult
import ru.piko.pikopluginlib.Utils.PikoAnnotation.NotRecommended
import java.util.*

enum class ChainResult {
	None,       // Ничего, следующая итерация на этот же элемент
	Break,      // Полностью заканчивает цепочку
	Next,       // Следующий элемент
	Back,       // Предыдущий элемент
	Again,      // Запускает с первого элемента
	Continue,   // Сразу же исполняет следующий элемент
	Previous,   // Сразу же исполняет предыдущий элемент
	AgainForce, // Сразу же запускает с первого элемента
	Next2,      // Перемещение на 2 элемента вперед
	Back2,      // Перемещение на 2 элемента назад
	Continue2,  // Немедленное выполнение элемента через один от текущего
	Previous2;  // Немедленное выполнение элемента за два до текущего
	
	fun toBuilderResult(): BuilderResult {
		return when (this) {
			None -> BuilderResult.None
			Break -> BuilderResult.Break
			Next -> BuilderResult.Next
			Back -> BuilderResult.Back
			Again -> BuilderResult.Again
			Continue -> BuilderResult.Continue
			Previous -> BuilderResult.Previous
			AgainForce -> BuilderResult.AgainForce
			Next2 -> BuilderResult.Next2
			Back2 -> BuilderResult.Back2
			Continue2 -> BuilderResult.Continue2
			Previous2 -> BuilderResult.Previous2
		}
	}
}

/**
 * Выполняет цепочку функций. Прекращает работу когда заканчивается список
 */
class FunctionChain private constructor(
	plugin: JavaPlugin,
	ticks: Long,
	delay: Long = 0,
	id: String = "",
	stopAllWithId: Boolean,
	val functions: List<() -> ChainResult>,
) : FunctionAbstract(plugin, ticks, delay, id, stopAllWithId) {
	private var functionIndex = 0
	
	override fun run() {
		// Запускаем текущую функцию и получаем результат
		val result = try {
			functions[functionIndex].invoke()
		} catch (e: Exception) {
			plugin.logger.warning("[PikoPluginLib] (${this::class.java}) Перехвачена ошибка в функции с id='$id'. Функция остановлена. Ошибка:")
			e.printStackTrace()
			ChainResult.Break
		}
		
		when (result) {
			ChainResult.None -> {}
			ChainResult.Next, ChainResult.Next2 -> {
				functionIndex += if (result == ChainResult.Next) 1 else 2
				if (functionIndex >= functions.size) {
					destroySelf()
				}
			}
			ChainResult.Back, ChainResult.Back2 -> {
				functionIndex -= if (result == ChainResult.Back) 1 else 2
				if (functionIndex < 0) {
					destroySelf()
				}
			}
			ChainResult.Previous, ChainResult.Previous2 -> {
				functionIndex -= if (result == ChainResult.Previous) 1 else 2
				if (functionIndex >= 0) {
					run()
				} else {
					destroySelf()
				}
			}
			ChainResult.Continue, ChainResult.Continue2 -> {
				functionIndex += if (result == ChainResult.Continue) 1 else 2
				if (functionIndex < functions.size) {
					run()
				} else {
					destroySelf()
				}
			}
			ChainResult.Break -> {
				destroySelf() // Уничтожаем цепочку
			}
			ChainResult.Again -> {
				functionIndex = 0 // Сбрасываем индекс
			}
			ChainResult.AgainForce -> {
				functionIndex = 0 // Сбрасываем индекс
				run() // Запускаем с первого элемента
			}
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
		val list: MutableList<FunctionChain> = Collections.synchronizedList(mutableListOf())
		
		fun create(
			plugin: JavaPlugin,
			ticks: Long,
			delay: Long = 0,
			id: String = "",
			stopAllWithId: Boolean = false,
			functions: List<() -> ChainResult>,
		): FunctionChain {
			if (stopAllWithId) {
				destroyAll(plugin, id)
			}
			
			val functionChain = FunctionChain(plugin, ticks, delay, id, stopAllWithId, functions)
			functionChain.initFunction()
			return functionChain
		}
		
		fun destroy(chain: FunctionChain) {
			chain.destroySelf()
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