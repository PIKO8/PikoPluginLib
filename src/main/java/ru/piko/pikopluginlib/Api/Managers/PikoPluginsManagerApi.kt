package ru.piko.pikopluginlib.Api.Managers

import ru.piko.pikopluginlib.Api.EStatusPlugin
import ru.piko.pikopluginlib.Api.PikoPluginAny
import ru.piko.pikopluginlib.Api.PikoPluginData
import ru.piko.pikopluginlib.Utils.InternalObject.main

class PikoPluginsManagerApi internal constructor() {
	
	private val pikoPluginDataMap = mutableMapOf<String, PikoPluginData>()
	
	val map get() = pikoPluginDataMap.toMap()
	
	fun get(id: String): PikoPluginData? = pikoPluginDataMap[id]
	
	fun disable(id: String) {
		pikoPluginDataMap[id]?.let { data ->
			if (!data.status.isDisable) {
				data.disable()
			}
		}
	}
	
	fun addDisable(id: String) {
		if (id !in pikoPluginDataMap) {
			pikoPluginDataMap[id] = PikoPluginData(id)
		}
	}
	
	fun add(id: String, pikoPlugin: PikoPluginAny) {
		(pikoPluginDataMap[id]).let { data ->
			val status = data?.status ?: EStatusPlugin.UNREGISTERED
			when {
				data == null || status.isUnregistered -> {
					pikoPluginDataMap[id] = PikoPluginData(id, pikoPlugin, pikoPlugin.blocked)
				}
				status.isEnable -> {
					println("PikoPlugin with ID: $id already registered.")
				}
				status.isBlocked -> {
					main.logger.warning("The plugin with ID: $id, which has a lock state, has probably been reloaded. This means that it is NOT RECOMMENDED to restart this plugin. Tip: restart the server completely.")
					data.activate(pikoPlugin, true)
				}
				status.isDisable -> {
					println("PikoPlugin with ID: $id is enabled")
					data.activate(pikoPlugin, pikoPlugin.blocked)
				}
			}
		}
	}
}
