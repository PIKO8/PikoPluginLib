package ru.piko.pikopluginlib.Functions

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import ru.piko.pikopluginlib.Functions.FunctionPeriodic.Companion
import java.util.*

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
		val list: MutableList<FunctionAbstract> = Collections.synchronizedList(mutableListOf())
		
		fun destroy(functionAbstract: FunctionAbstract) {
			functionAbstract.destroySelf()
			list.remove(functionAbstract)
		}
		
		fun destroyAll(plugin: JavaPlugin) {
			val itemsToRemove = mutableListOf<FunctionAbstract>()
			val iterator = list.iterator()
			while (iterator.hasNext()) {
				val item = iterator.next()
				if (item.plugin == plugin) {
					itemsToRemove.add(item)
				}
			}
			for (item in itemsToRemove) {
				item.destroySelf()
				list.remove(item)
			}
		}
		
		fun <T: FunctionAbstract> destroyAll(list: MutableList<T>, id: String) {
			val itemsToRemove = mutableListOf<T>()
			val iterator = list.iterator()
			while (iterator.hasNext()) {
				val item = iterator.next()
				if (item.id == id) itemsToRemove.add(item)
			}
			for (item in itemsToRemove) {
				item.destroySelf()
				list.remove(item)
			}
		}
		
		fun <T: FunctionAbstract> destroyAll(list: MutableList<T>, plugin: JavaPlugin, id: String) {
			val itemsToRemove = mutableListOf<T>()
			val iterator = list.iterator()
			while (iterator.hasNext()) {
				val item = iterator.next()
				if (item.plugin == plugin && item.id == id) {
					itemsToRemove.add(item)
				}
			}
			for (item in itemsToRemove) {
				item.destroySelf()
				list.remove(item)
			}
		}
	}
}