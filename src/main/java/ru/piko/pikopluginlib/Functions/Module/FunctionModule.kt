package ru.piko.pikopluginlib.Functions.Module

import ru.piko.pikopluginlib.Functions.Module.AbstractModule.AbstractModule
import ru.piko.pikopluginlib.Functions.Module.Instances.ModuleDestroy

class FunctionModule : IBaseModule {
	
	
	override var modules: MutableMap<String, AbstractModule> = mutableMapOf()
	
	override var onDestroy: ModuleDestroy? = null
		private set
	
	
	companion object {
	
	
	}
	
}
