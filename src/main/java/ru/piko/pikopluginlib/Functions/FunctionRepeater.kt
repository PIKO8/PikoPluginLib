package ru.piko.pikopluginlib.Functions

import org.bukkit.plugin.java.JavaPlugin

class FunctionRepeater private constructor(plugin: JavaPlugin, ticks: Long, val maxRepeats: Int, delay: Long = 0, id: String = "", stopAllWithId: Boolean, val function: () -> Unit)
    : FunctionAbstract(plugin, ticks, delay, id, stopAllWithId) {
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
        val list : MutableList<FunctionRepeater> = ArrayList()

        fun create(plugin: JavaPlugin, ticks: Long, maxRepeats: Int, delay: Long = 0, id: String = "", stopAllWithId: Boolean = false, function: () -> Unit): FunctionRepeater {
            if (stopAllWithId) {
                destroyAll(plugin, id)
            }

            val functionRepeater = FunctionRepeater(plugin, ticks, maxRepeats, delay, id, stopAllWithId, function)
            functionRepeater.initFunction()
            return functionRepeater
        }

        fun destroy(repeater: FunctionRepeater) {
            FunctionAbstract.destroy(repeater)
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