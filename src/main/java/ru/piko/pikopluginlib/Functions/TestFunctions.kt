package ru.piko.pikopluginlib.Functions

import org.bukkit.plugin.java.JavaPlugin

class TestFunctions {
    companion object Static {
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
                { println("Функция 1 выполнена!") },
                { println("Функция 2 выполнена!") },
                { println("Функция 3 выполнена!") }
            ))

            val anotherFunctionChain = FunctionChain.create(plugin, 20, 0, "testId", functions = listOf(
                { println("Другая функция 1 выполнена!") },
                { println("Другая функция 2 выполнена!") },
                { println("Другая функция 3 выполнена!") }
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
    }
}