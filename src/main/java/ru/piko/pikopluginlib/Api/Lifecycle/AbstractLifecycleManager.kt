package ru.piko.pikopluginlib.Api.Lifecycle

import ru.piko.pikopluginlib.Api.PikoPlugin
import ru.piko.pikopluginlib.Utils.Extends.Language.ThrowableExtend.bukkitSevere
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.CopyOnWriteArrayList

typealias AbstractLifecycleManagerAny =
		AbstractLifecycleManager<*, *>

abstract class AbstractLifecycleManager<Impl: AbstractLifecycleManager<Impl, Event>, Event : AbstractLifecycleEvent<Event, Impl>>
{
	val eventMap: ConcurrentMap<AbstractLifecyclePoint, MutableList<Event>> = ConcurrentHashMap()
	
	open fun invoke(point: AbstractLifecyclePoint) {
		this as Impl
		eventMap[point]?.sortedBy { it.priority }?.forEach { event ->
			event.execute(this)
		}
	}
	
	fun register(point: AbstractLifecyclePoint, event: Event) {
		eventMap.getOrPut(point) { CopyOnWriteArrayList() }.add(event)
	}
	
	fun getEvents(point: AbstractLifecyclePoint): List<Event> = eventMap[point]?.toList() ?: emptyList()
}

class PluginLifecycleManager<Plugin : PikoPlugin<Plugin>>(val plugin: Plugin) : AbstractLifecycleManager<PluginLifecycleManager<Plugin>, PluginLifecycleEvent<Plugin>>() {
	
	override fun invoke(point: AbstractLifecyclePoint) {
		eventMap[point]?.sortedBy { it.priority }?.forEach { event ->
			try {
				event.execute(this)
			} catch (e: Exception) {
				e.bukkitSevere(plugin, "In Lifecycle error! Point: '$point' Priority: '${event.priority}'. (The error is caught, the process continues)")
			}
		}
	}
	
}