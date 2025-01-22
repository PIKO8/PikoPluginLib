package ru.piko.pikopluginlib.Functions

import org.bukkit.plugin.java.JavaPlugin
import java.util.*

abstract class FunctionAbstract(
	val plugin: JavaPlugin,
	val id: String = "",
	val stopAllWithId: Boolean,
) {
	
	abstract fun destroySelf()
	protected abstract fun init()
	
	protected abstract fun initAbstract()
	
	public fun initFunction() {
		initAbstract()
		list.add(this)
		init()
	}
	
	companion object {
		val list: MutableList<FunctionAbstract> = Collections.synchronizedList(mutableListOf())
		
		fun destroy(functionAbstract: FunctionAbstract) {
			functionAbstract.destroySelf()
		}
		
		fun destroyAll(plugin: JavaPlugin) {
			iterator(list.iterator()) { it.plugin == plugin }
		}
		
		fun <T: FunctionAbstract> removeObjectInList(list: MutableList<T>, obj: FunctionAbstract) {
			synchronized(list) {
				list.remove(obj)
			}
			synchronized(this.list) {
				this.list.remove(obj)
			}
		}
		
		fun <T: FunctionAbstract> destroyAll(list: MutableList<T>, id: String) {
			iterator(list.iterator()) { it.id == id }
		}
		
		fun <T: FunctionAbstract> destroyAll(list: MutableList<T>, plugin: JavaPlugin, id: String) {
			iterator(list.iterator()) { it.plugin == plugin && it.id == id }
		}
		
		private fun <T : FunctionAbstract> iterator(iterator: Iterator<T>, predicate: (T) -> Boolean) {
			val itemsToRemove = mutableListOf<T>()
			while (iterator.hasNext()) {
				val item = iterator.next()
				if (predicate(item)) {
					itemsToRemove.add(item)
				}
			}
			for (item in itemsToRemove) {
				item.destroySelf()
			}
		}
	}
}