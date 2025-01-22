package ru.piko.pikopluginlib.Functions

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask

abstract class FunctionAbstractBukkitScheduler(
	plugin: JavaPlugin,
	val ticks: Long,
	val delay: Long,
	id: String = "",
	stopAllWithId: Boolean,
) : FunctionAbstract(plugin, id, stopAllWithId) {
	var task: BukkitTask? = null
	
	abstract fun run()
	
	override fun initAbstract() {
		task = Bukkit.getScheduler().runTaskTimer(plugin, this::run, delay, ticks)
	}
	
}