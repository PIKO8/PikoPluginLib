package ru.piko.pikopluginlib.Functions

import org.bukkit.plugin.java.JavaPlugin

enum class ChainResult {
    None, // Ничего
    Continue, // сразу же исполняет следующий элемент
    Break, // Полностью заканчивает цепочку
    Again // Запускает с первого элемента
}

/**
 * Выполняет цепочку функций. Прекращает работу когда заканчивается список
 * FunctionChain - TODO Возвращать не Unit а enum: None, Continue, Break, Again(Ещё раз перебирать список)
 */
class FunctionChain private constructor(plugin: JavaPlugin, ticks: Long, delay: Long = 0, id: String = "", stopAllWithId: Boolean, val functions: List<() -> ChainResult>)
    : FunctionAbstract(plugin, ticks, delay, id, stopAllWithId) {
    private var functionIndex = 0

    override fun run() {
        // Запускаем текущую функцию и получаем результат
        val result = functions[functionIndex].invoke()

        when (result) {
            ChainResult.None -> {
                functionIndex++
                if (functionIndex >= functions.size) {
                    destroySelf() // Если это последний элемент, уничтожаем цепочку
                }
            }
            ChainResult.Continue -> {
                functionIndex++
                if (functionIndex < functions.size) {
                    run() // Запускаем следующую функцию
                } else {
                    destroySelf() // Если это последний элемент, уничтожаем цепочку
                }
            }
            ChainResult.Break -> {
                destroySelf() // Уничтожаем цепочку
            }
            ChainResult.Again -> {
                functionIndex = 0 // Сбрасываем индекс
                run() // Запускаем с первого элемента
            }
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
        val list : MutableList<FunctionChain> = ArrayList()

        fun create(plugin: JavaPlugin, ticks: Long, delay: Long = 0, id: String = "", stopAllWithId: Boolean = false, functions: List<() -> ChainResult>): FunctionChain {
            if (stopAllWithId) {
                destroyAll(plugin, id)
            }

            val functionChain = FunctionChain(plugin, ticks, delay, id, stopAllWithId, functions)
            functionChain.initFunction()
            return functionChain
        }

        fun destroy(chain: FunctionChain) {
            FunctionAbstract.destroy(chain)
        }

        @NotRecommended("Может сломать что-нибудь в других плагинах лучше использовать destroyAll(plugin: JavaPlugin, id: String)")
        @Deprecated("Не рекомендованный")
        fun destroyAll(id: String) {
            list.forEach {
                if (it.id == id) { it.destroySelf() }
            }
        }

        fun destroyAll(plugin: JavaPlugin, id: String) {
            list.forEach {
                if (it.plugin == plugin && it.id == id) { it.destroySelf() }
            }
        }
    }
}