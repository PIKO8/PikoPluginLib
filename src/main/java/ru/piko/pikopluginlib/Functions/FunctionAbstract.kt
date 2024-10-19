package ru.piko.pikopluginlib.Functions

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask

abstract class FunctionAbstract(
	val plugin: JavaPlugin,
	val ticks: Long,
	val delay: Long,
	val id: String = "",
	val stopAllWithId: Boolean,
) {
	var task: BukkitTask? = null
	
	abstract fun run()
	abstract fun destroySelf()
	protected abstract fun init()
	
	public fun initFunction() {
		task = Bukkit.getScheduler().runTaskTimer(plugin, this::run, delay, ticks)
		list.add(this)
		init()
	}
	
	companion object Static {
		val list: MutableList<FunctionAbstract> = mutableListOf()
		
		fun destroy(functionAbstract: FunctionAbstract) {
			functionAbstract.destroySelf()
		}
		
		fun destroyAll(plugin: JavaPlugin) {
			list.forEach {
				if (it.plugin == plugin) { it.destroySelf() }
			}
		}
		
	}
}