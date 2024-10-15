package ru.piko.pikopluginlib.Functions

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask

abstract class FunctionAbstract(val plugin: JavaPlugin, val ticks: Long, val delay: Long, val id: String = "", val stopAllWithId: Boolean) {
    var task: BukkitTask? = null

    abstract fun run()
    abstract fun destroySelf()
    protected abstract fun init()

    public fun initFunction() {
        task = Bukkit.getScheduler().runTaskTimer(plugin, this::run, delay, ticks)
        init()
    }

    companion object Static {
        fun destroy(functionAbstract: FunctionAbstract) {
            functionAbstract.destroySelf()
        }
    }
}