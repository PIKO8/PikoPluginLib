package ru.piko.pikopluginlib.Functions

import io.papermc.paper.event.executor.EventExecutorFactory
import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import java.lang.reflect.Method
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.jvm.javaMethod

class FunctionEvent<E : Event> constructor(
	plugin: JavaPlugin,
	val eventClass: KClass<E>,
	val priority: EventPriority = EventPriority.NORMAL,
	val ignoreCancelled: Boolean = false,
	id: String = "",
	stopAllWithId: Boolean = false,
	val function: (E) -> Unit
) : FunctionAbstract(plugin, id, stopAllWithId) {
	private var listener: IEvent<E>? = null
	
	fun run(event: E) {
		try {
			function.invoke(event)
		} catch (e: Exception) {
			plugin.logger.warning("[PikoPluginLib] (${this::class.java}) Перехвачена ошибка в функции с id='$id'. Ошибка:")
			e.printStackTrace()
		}
	}
	
	override fun destroySelf() {
		listener?.let { HandlerList.unregisterAll(it) }
		FunctionAbstract.removeObjectInList(FunctionTimer.list, this)
	}
	
	override fun init() {
		synchronized(list) {
			list.add(this)
		}
	}
	
	private interface IEvent<E : Event> : Listener {
		@EventHandler
		fun onEvent(event : E)
		
		fun getEvent(): Method?
	}
	
	override fun initAbstract() {
		listener = object : IEvent<E> {
			override fun onEvent(event: E) {
				run(event)
			}
			
			override fun getEvent(): Method? {
				return this::onEvent.javaMethod
			}
		}
		val safe = listener ?: throw IllegalStateException("FunctionEvent listener is null")
		val method: Method = safe.getEvent()!!
		registerEvent(plugin, safe, method, eventClass.java, priority, ignoreCancelled)
	}
	
	companion object : ICompanionFunction<FunctionEvent<*>> {
		override val list: MutableList<FunctionEvent<*>> = Collections.synchronizedList(mutableListOf())
		
		fun <E : Event> create(
			plugin: JavaPlugin,
			eventClass: KClass<E>,
			priority: EventPriority = EventPriority.NORMAL,
			ignoreCancelled: Boolean = false,
			id: String = "",
			stopAllWithId: Boolean = false,
			function: (E) -> Unit,
		): FunctionEvent<E> {
			if (stopAllWithId) {
				destroyAll(plugin, id)
			}
			
			val functionEvent = FunctionEvent<E>(plugin, eventClass, priority, ignoreCancelled, id, stopAllWithId, function)
			functionEvent.initFunction()
			return functionEvent
			
		}
		
		@Suppress("UnstableApiUsage")
		private fun registerEvent(
			plugin: JavaPlugin,
			listener : Listener,
			method: Method,
			eventClass: Class<out Event>,
			priority: EventPriority,
			ignoreCancelled: Boolean
		) {
			Bukkit.getPluginManager().registerEvent(
				eventClass,
				listener,
				priority,
				EventExecutorFactory.create(method, eventClass),
				plugin,
				ignoreCancelled
			)
		}
		
	}
}