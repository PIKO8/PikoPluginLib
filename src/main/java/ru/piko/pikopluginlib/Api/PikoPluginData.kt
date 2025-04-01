package ru.piko.pikopluginlib.Api

import java.io.File

class PikoPluginData {
	val id: String
	var namePlugin: String?
		private set
	var countLoad: Int = 0
		private set
	var plugin: PikoPluginAny? = null
		private set
	var status: EStatusPlugin // Статус плагина
		private set
	val file: File? // Нужен, что бы можно было включить плагин
	
	internal constructor(id: String, plugin: PikoPluginAny, blocked: Boolean) {
		this.id = id
		this.plugin = plugin
		this.status = if (blocked) EStatusPlugin.BLOCKED_ENABLE else EStatusPlugin.ENABLE
		this.file = plugin.pluginFile
		this.namePlugin = plugin.name
	}
	
	internal constructor(id: String) {
		this.id = id
		this.plugin = null
		this.status = EStatusPlugin.DISABLE
		this.file = null
		this.namePlugin = null
	}
	
	internal fun addCount() {
		countLoad += 1
	}
	
	fun isFirstLoad(): Boolean = countLoad == 0
	
	fun activate(plugin: PikoPluginAny?, blocked: Boolean) {
		this.plugin = plugin
		status = if (blocked) EStatusPlugin.BLOCKED_ENABLE else EStatusPlugin.ENABLE
		if (namePlugin == null) namePlugin = plugin?.name
	}
	
	fun disable() {
		plugin = null
		status = if (status.isBlocked) EStatusPlugin.BLOCKED_DISABLE else EStatusPlugin.DISABLE
	}
}