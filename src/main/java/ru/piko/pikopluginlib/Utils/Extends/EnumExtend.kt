package ru.piko.pikopluginlib.Utils.Extends

/**
 * Возвращает значение перечисления по имени или null, если такого значения не существует
 * @return Значение перечисления или null
 */
fun <T : Enum<T>> enumValueOrNull(enumClass: Class<T>, name: String): T? {
	return try {
		java.lang.Enum.valueOf(enumClass, name)
	} catch (e: IllegalArgumentException) {
		null
	}
}