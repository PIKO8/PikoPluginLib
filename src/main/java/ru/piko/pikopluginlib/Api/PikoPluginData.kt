package ru.piko.pikopluginlib.Api

import java.io.File

class PikoPluginData {
	val id: String
	val namePlugin: String?
	var plugin: PikoPlugin? = null
	var status: EStatusPlugin // Статус плагина
	val file: File? // Нужен, что бы можно было включить плагин
	
	constructor(id: String, plugin: PikoPlugin, blocked: Boolean) {
		this.id = id
		this.plugin = plugin
		this.status = if (blocked) EStatusPlugin.BLOCKED_ENABLE else EStatusPlugin.ENABLE
		this.file = plugin.pluginFile
		this.namePlugin = plugin.name
	}
	
	constructor(id: String) {
		this.id = id
		this.plugin = null
		this.status = EStatusPlugin.DISABLE
		this.file = null
		this.namePlugin = null
	}
	
	fun activate(plugin: PikoPlugin?, blocked: Boolean) {
		this.plugin = plugin
		status = if (blocked) EStatusPlugin.BLOCKED_ENABLE else EStatusPlugin.ENABLE
	}
	
	fun disable() {
		plugin = null
		status = if (status.isBlocked) EStatusPlugin.BLOCKED_DISABLE else EStatusPlugin.DISABLE
	}
}