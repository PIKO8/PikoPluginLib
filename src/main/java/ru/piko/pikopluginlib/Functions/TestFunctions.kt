package ru.piko.pikopluginlib.Functions

import org.bukkit.plugin.java.JavaPlugin
import ru.piko.pikopluginlib.Utils.Edit
import ru.piko.pikopluginlib.Utils.MadeAI

class TestFunctions {
	companion object Static {
    @MadeAI(ai = Edit.Many, human = Edit.Minimum)
		fun test(plugin: JavaPlugin) {
			// Тестирование FunctionTimer
			val functionTimer = FunctionTimer.create(plugin, 1000, "testId") {
				println("Функция выполнена!")
			}
			
			val anotherFunctionTimer = FunctionTimer.create(plugin, 2000, "testId") {
				println("Другая функция выполнена!")
			}
			FunctionTimer.destroyAll(plugin, "testId")
			
			println("Количество FunctionTimer: ${FunctionTimer.list.size}")
			
			// Тестирование FunctionPeriodic
			val functionPeriodic = FunctionPeriodic.create(plugin, 10, 0, "testId") {
				println("Функция выполнена!")
				true // продолжать выполнение
			}
			
			val anotherFunctionPeriodic = FunctionPeriodic.create(plugin, 20, 0, "testId") {
				println("Другая функция выполнена!")
				false // остановить выполнение
			}
			FunctionPeriodic.destroyAll(plugin, "testId")
			
			println("Количество FunctionPeriodic: ${FunctionPeriodic.list.size}")
			
			// Тестирование FunctionRepeater
			val functionRepeater = FunctionRepeater.create(plugin, 10, 3, 0, "testId") {
				println("Функция выполнена!")
			}
			
			val anotherFunctionRepeater = FunctionRepeater.create(plugin, 20, 2, 0, "testId") {
				println("Другая функция выполнена!")
			}
			FunctionRepeater.destroyAll(plugin, "testId")
			
			println("Количество FunctionRepeater: ${FunctionRepeater.list.size}")
			
			// Тестирование FunctionConditional
			val functionConditional = FunctionConditional.create(plugin, 10, 0, "testId", condition = { true }) {
				println("Функция выполнена!")
			}
			
			val anotherFunctionConditional = FunctionConditional.create(plugin, 20, 0, "testId", condition = { false }) {
				println("Другая функция выполнена!")
			}
			FunctionConditional.destroyAll(plugin, "testId")
			
			println("Количество FunctionConditional: ${FunctionConditional.list.size}")
			
			// Тестирование FunctionChain
			val functionChain = FunctionChain.create(plugin, 10, 0, "testId", functions = listOf(
				{ println("Функция 1 выполнена!"); return@listOf ChainResult.Next },
				{ println("Функция 2 выполнена!"); return@listOf ChainResult.Next },
				{ println("Функция 3 выполнена!"); return@listOf ChainResult.Next }
			))
			
			val anotherFunctionChain = FunctionChain.create(plugin, 20, 0, "testId", functions = listOf(
				{ println("Другая функция 1 выполнена!"); return@listOf ChainResult.Next },
				{ println("Другая функция 2 выполнена!"); return@listOf ChainResult.Next },
				{ println("Другая функция 3 выполнена!"); return@listOf ChainResult.Next }
			))
			
			FunctionChain.destroyAll(plugin, "testId")
			
			println("Количество FunctionChain: ${FunctionChain.list.size}")
			
			val timer = FunctionTimer.create(plugin, 500) {
				println("Количество FunctionTimer: ${FunctionTimer.list.size}")
				println("Количество FunctionPeriodic: ${FunctionPeriodic.list.size}")
				println("Количество FunctionRepeater: ${FunctionRepeater.list.size}")
				println("Количество FunctionConditional: ${FunctionConditional.list.size}")
				println("Количество FunctionChain: ${FunctionChain.list.size}")
			}
		}
		
		@MadeAI(ai = Edit.Many, human = Edit.False)
		fun complexFunctionBuilderExample(plugin: JavaPlugin) {
			FunctionBuilder.create(plugin, ticks = 20L, delay = 40L, id = "Example")
				.functionUnit { // Пустая функция
					it["my_var"] = 0 // Новая переменная
				}
				.functionUnit {
					val my = it["my_var"] as? Int ?: 0
					println("my_var = $my")
					it["my_var"] = my + 1
				}
				.conditionSkip(stepFalse = 1, stepTrue = 2) { // при False
					val my = it["my_var"] as? Int ?: 0
					my >= 10
				}
				.function { // False функция
					println("false - my_far < 10") // Это false my_var < 10
					BuilderResult.Next2 // Скачок через функцию
				}
				.functionUnit { // True функция
					println("true - my_far >= 10") // Это false my_var >= 10
					BuilderResult.Next // Следующая функция
				}
				.functionUnit { // Общая функция
					val i = it[FunctionBuilder.INDEX] as Int
					print("deleted 0 and $i functions")// Удаляем 0 и эту функцию
					it[FunctionBuilder.REMOVE_FUNCTION] = listOf(0, i) // Удаление 0 функции не будет всегда вызывать
				}
				.function {
					val my = it["my_var"] as? Int ?: 0
					if (my >= 20 ) BuilderResult.Break else BuilderResult.Again // если = 20 удаляем иначе начинаем с начала
				}
				.invoke()
		}
	}
}