package ru.piko.pikopluginlib.Functions.Module

import ru.piko.pikopluginlib.Functions.Module.AbstractModule.AbstractModule
import ru.piko.pikopluginlib.Functions.Module.Instances.ModuleDestroy

interface IBaseModule {
	
	var modules: MutableMap<String, AbstractModule>
	val onDestroy: ModuleDestroy?
	
}