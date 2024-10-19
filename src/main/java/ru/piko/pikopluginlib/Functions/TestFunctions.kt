@file:Suppress("UNCHECKED_CAST")

package ru.piko.pikopluginlib.Functions

import org.bukkit.plugin.java.JavaPlugin
import ru.piko.pikopluginlib.Functions.Builder.*
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
				{ println("Функция 1 выполнена!"); ChainResult.Next },
				{ println("Функция 2 выполнена!"); ChainResult.Next },
				{ println("Функция 3 выполнена!"); ChainResult.Next }
			))
			
			val anotherFunctionChain = FunctionChain.create(plugin, 20, 0, "testId", functions = listOf(
				{ println("Другая функция 1 выполнена!"); ChainResult.Next },
				{ println("Другая функция 2 выполнена!"); ChainResult.Next },
				{ println("Другая функция 3 выполнена!"); ChainResult.Next }
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
		
		@MadeAI(ai = Edit.False, human = Edit.Many)
		fun complexFunctionBuilderExample(plugin: JavaPlugin) {
			FunctionBuilder.create(plugin, ticks = 20L, delay = 40L, id = "Example")
				.functionUnit { // Пустая функция
					it["my_var"] = 0 // Новая переменная
				}
				.functionUnit {
					var my = it["my_var"] as? Int ?: 0
					my += 1 // Прибавляем 1
					println("my_var = $my") // Выводим
					it["my_var"] = my // Записываем
				}
				.conditionSkip(stepFalse = 1, stepTrue = 2) { // при False
					val my = it.getAs<Int>("my_var") ?: 0 // Можно вот так получить значение
					my >= 4 // Проверка на то что my больше или равно 4
				}
				.function { // False функция
					println("false - my_var < 4") // Это false my_var < 4
					BuilderResult.Next2 // Скачок через функцию
				}
				.functionUnit { // True функция
					println("true - my_var >= 4") // Это false my_var >= 4
					BuilderResult.Next // Следующая функция
				}
				.functionUnit { // Общая функция
					val i = it.index // Получаем индекс текущей функции
					val functions = it.functions // Получаем список функций
					println("deleted 0 and $i functions") // Удаляем 0 и эту функцию
					functions.removeAt(i) // Удаляем эту функцию
					functions.removeFirst() // Удаляем 0 функцию из списка
				}
				.function { data: BuilderData -> // Можно так data получать
					val my = data.getAs<Int>("my_var") ?: 0 // Можно вот так получить значение
					if (my >= 8) { // если = 8
						println("break")
						BuilderResult.Break // Полностью останавливаем цепочку
					} else {
						BuilderResult.Again //  начинаем с начала
					}
				}
				.invoke()
		}
	}
}