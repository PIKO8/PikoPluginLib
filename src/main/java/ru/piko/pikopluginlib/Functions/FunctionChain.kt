package ru.piko.pikopluginlib.Functions

import org.bukkit.plugin.java.JavaPlugin

/**
 * Выполняет цепочку функций
 * TODO Возвращать не Unit а enum: None, Continue, Break
 */
class FunctionChain private constructor(plugin: JavaPlugin, ticks: Long, delay: Long = 0, id: String = "", stopAllWithId: Boolean, val functions: List<() -> Unit>)
    : FunctionAbstract(plugin, ticks, delay, id, stopAllWithId) {
    private var functionIndex = 0

    override fun run() {
        functions[functionIndex].invoke()
        functionIndex++
        if (functionIndex >= functions.size) {
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
        val list : MutableList<FunctionChain> = ArrayList()

        fun create(plugin: JavaPlugin, ticks: Long, delay: Long = 0, id: String = "", stopAllWithId: Boolean = false, functions: List<() -> Unit>): FunctionChain {
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