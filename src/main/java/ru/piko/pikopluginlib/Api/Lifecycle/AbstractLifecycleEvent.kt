package ru.piko.pikopluginlib.Api.Lifecycle

import ru.piko.pikopluginlib.Api.PikoPlugin

abstract class AbstractLifecycleEvent<Impl : AbstractLifecycleEvent<Impl, Manager>, Manager : AbstractLifecycleManager<Manager, Impl>>(val priority: Int) {
	
	abstract fun execute(manager: Manager)
	
}

class PluginLifecycleEvent<Plugin : PikoPlugin<Plugin>>(
	priority: Int,
	val function: (Plugin) -> Unit
) : AbstractLifecycleEvent<PluginLifecycleEvent<Plugin>, PluginLifecycleManager<Plugin>>(priority) {
	override fun execute(manager: PluginLifecycleManager<Plugin>) {
		function(manager.plugin)
	}
}