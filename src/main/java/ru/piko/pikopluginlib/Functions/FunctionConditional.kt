package ru.piko.pikopluginlib.Functions

import org.bukkit.plugin.java.JavaPlugin

/**
 * Сработает один раз когда выполнено condition и выполнит function
 */
class FunctionConditional private constructor(plugin: JavaPlugin, ticks: Long, delay: Long = 0, id: String = "", stopAllWithId: Boolean, val condition: () -> Boolean, val function: () -> Unit)
    : FunctionAbstract(plugin, ticks, delay, id, stopAllWithId) {
    override fun run() {
        if (condition.invoke()) {
            function.invoke()
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
        val list : MutableList<FunctionConditional> = ArrayList()

        fun create(plugin: JavaPlugin, ticks: Long, delay: Long = 0, id: String = "", stopAllWithId: Boolean = false, condition: () -> Boolean, function: () -> Unit): FunctionConditional {
            if (stopAllWithId) {
                destroyAll(plugin, id)
            }

            val functionConditional = FunctionConditional(plugin, ticks, delay, id, stopAllWithId, condition, function)
            functionConditional.initFunction()
            return functionConditional
        }

        fun destroy(conditional: FunctionConditional) {
            FunctionAbstract.destroy(conditional)
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