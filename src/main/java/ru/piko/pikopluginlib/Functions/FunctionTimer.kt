package ru.piko.pikopluginlib.Functions

import org.bukkit.plugin.java.JavaPlugin

/**
 * Сработает один раз через delay
 */
class FunctionTimer private constructor(plugin: JavaPlugin, delay: Long, id: String, stopAllWithId: Boolean, val function: () -> Unit) : FunctionAbstract(plugin, 1, delay, id, stopAllWithId) {
    override fun run() {
        function.invoke()
        destroySelf()
    }

    override fun destroySelf() {
        task?.cancel()
        list.remove(this)
    }

    override fun init() {
        list.add(this)
    }


    companion object {
        val list : MutableList<FunctionTimer> = ArrayList()

        fun create(plugin: JavaPlugin, delay: Long, id: String = "", stopAllWithId: Boolean = false, function: () -> Unit): FunctionTimer {
            if (stopAllWithId) {
                destroyAll(plugin, id)
            }

            val functionTimer = FunctionTimer(plugin, delay, id, stopAllWithId, function)
            functionTimer.initFunction()
            return functionTimer
        }

        fun destroy(timer: FunctionTimer) {
            FunctionTimer.destroy(timer)
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