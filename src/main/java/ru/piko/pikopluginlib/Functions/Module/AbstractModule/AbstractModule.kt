package ru.piko.pikopluginlib.Functions.Module.AbstractModule

import ru.piko.pikopluginlib.Functions.Module.FunctionModule
import ru.piko.pikopluginlib.Functions.Module.IBaseModule
import ru.piko.pikopluginlib.Functions.Module.Instances.ModuleDestroy

abstract class AbstractModule : IBaseModule {
	
	val function: FunctionModule
	val parent: IBaseModule
	
	override var onDestroy: ModuleDestroy? = null
	
	constructor(parent: FunctionModule) {
		this.function = parent
		this.parent = parent
	}
	
	constructor(function: FunctionModule, parent: IBaseModule) {
		this.function = function
		this.parent = parent
	}
	
	val hasFunctionParent: Boolean get() = function == parent // TODO Уточнить что тут has или is будет правильно
	
	val isParentModule: Boolean get() = parent is AbstractModule
	
	val parentModule: AbstractModule get() =
		(if (isParentModule) parent as? AbstractModule else null)
			?: throw TODO("Вызвать ошибку что сначала нужно проверять isParentModule")
	
	
}