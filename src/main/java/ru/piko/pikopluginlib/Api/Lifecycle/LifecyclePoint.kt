package ru.piko.pikopluginlib.Api.Lifecycle

abstract class AbstractLifecyclePoint {
	
	abstract override fun equals(other: Any?): Boolean
	
}

class LifecyclePoint private constructor(private val value: ELifePoint) : AbstractLifecyclePoint() {
	
	companion object {
		
		val LOAD = LifecyclePoint(ELifePoint.LOAD)
		val START = LifecyclePoint(ELifePoint.START)
		val STOP = LifecyclePoint(ELifePoint.STOP)
		val CONFIG_RELOAD = LifecyclePoint(ELifePoint.CONFIG_RELOAD)
		
	}
	
	private enum class ELifePoint {
		LOAD,
		START,
		STOP,
		CONFIG_RELOAD
	}
	
	val name: String = value.name
	
	override fun equals(other: Any?): Boolean {
		if (other is LifecyclePoint) return this.value == other.value
		return false
	}
	
	override fun hashCode(): Int {
		return value.hashCode()
	}
	
}