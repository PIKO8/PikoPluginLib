package ru.piko.pikopluginlib.Files.Abstract

abstract class AbstractResource(val name: String) {
	
	abstract fun load(reload: Boolean = false)
	abstract fun save()
	
	
}