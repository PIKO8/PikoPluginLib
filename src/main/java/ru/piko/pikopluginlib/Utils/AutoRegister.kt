package ru.piko.pikopluginlib.Utils

import org.bukkit.plugin.java.JavaPlugin
import org.reflections.Reflections

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class AutoRegister

object AutoRegisterObject {
	
	fun load(plugin: JavaPlugin, classLoader: ClassLoader, packageName: String) {
		Reflections(packageName, classLoader)
			.getTypesAnnotatedWith(AutoRegister::class.java)
			.forEach { clazz ->
				clazz.kotlin.objectInstance?.run {
					plugin.logger.info("Auto-registered: ${clazz.simpleName}")
				}
			}
	}
}