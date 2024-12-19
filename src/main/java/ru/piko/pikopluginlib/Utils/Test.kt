package ru.piko.pikopluginlib.Utils

import ru.piko.pikopluginlib.Utils.FormatExtend.toFormatAs
import ru.piko.pikopluginlib.Utils.FormatExtend.toFormatSafeAs

object Test {
	
	fun test() {
		val t1 = listOf(1, 0, 1, 1)
		println("FormatExtendTest: t1 = $t1")
		val bool = t1.toFormatAs<List<Boolean>>()
		println("FormatExtendTest: bool = $bool")
		val float = bool.toFormatSafeAs<List<Float>>()
		println("FormatExtendTest: float = $float")
	}
 
}