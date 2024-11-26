package ru.piko.pikopluginlib.Utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.typeOf

@Suppress("IMPLICIT_CAST_TO_ANY")
inline fun <reified Type : Any> Any.toFormatAs(): Type? {
	return toFormat(typeOf<Type>()) as? Type
}

fun Any.toFormat(type: KType): Any? {
	val targetClass = type.classifier as? KClass<*> ?: return null
	
	// Обработка коллекций
	if (this is Collection<*> || this is Array<*>) {
		val collection = when (this) {
			is List<*> -> this
			is Set<*> -> this.toList()
			is Array<*> -> this.asList()
			else -> return null
		}
		
		return when {
			targetClass.isSubclassOf(Set::class) -> collection.mapNotNull { it?.toFormat(type.getTypeArguments().firstOrNull() ?: return null) }.toSet()
			targetClass.isSubclassOf(Array::class) -> collection.mapNotNull { it?.toFormat(type.getTypeArguments().firstOrNull() ?: return null) }.toTypedArray()
			targetClass.isSubclassOf(List::class) -> collection.mapNotNull { it?.toFormat(type.getTypeArguments().firstOrNull() ?: return null) }
			else -> null
		}
	}
	
	
	// Обработка коллекций
	if ((this is List<*> || this is Array<*>) && targetClass.isSubclassOf(List::class)) {
		val list = if (this is Array<*>) this.toList() else this as List<*>
		
		val targetType = try {
			type.getTypeArguments().firstOrNull()
		} catch (e: Exception) {
			println("Type detection error: ${e.message}")
			e.printStackTrace()
			null
		}
		
		return when (targetType) {
			null -> {
				println("Тип у списка не найден")
				null
			}
			typeOf<Byte>() -> list.map { it?.toFormatAs<Byte>() }
			typeOf<Short>() -> list.map { it?.toFormatAs<Short>() }
			typeOf<Int>() -> list.map { it?.toFormatAs<Int>() }
			typeOf<Long>() -> list.map { it?.toFormatAs<Long>() }
			typeOf<Float>() -> list.map { it?.toFormatAs<Float>() }
			typeOf<Double>() -> list.map { it?.toFormatAs<Double>() }
			typeOf<String>() -> list.map { it?.toFormatAs<String>() }
			typeOf<Boolean>() -> list.map { it?.toFormatAs<Boolean>() }
			typeOf<LocalDate>() -> list.map { it?.toFormatAs<LocalDate>() }
			typeOf<LocalDateTime>() -> list.map { it?.toFormatAs<LocalDateTime>() }
			else -> {
				println("Тип у списка не найден")
				null
			}
		}
	}
	
	return when {
		// Проверка на совпадение типов
		targetClass.isInstance(this) -> this
		
		// Конвертация из String
		this is String -> when (targetClass) {
			Byte::class -> tryParseByte()
			Short::class -> tryParseShort()
			Int::class -> tryParseInt()
			Long::class -> tryParseLong()
			Float::class -> tryParseFloat()
			Double::class -> tryParseDouble()
			Boolean::class -> tryParseBoolean()
			Char::class -> firstOrNull()
			LocalDate::class -> tryParseLocalDate()
			LocalDateTime::class -> tryParseLocalDateTime()
			List::class -> {
				val targetType = type.getTypeArguments().firstOrNull()
				targetType?.let { tt ->
					split(",").map { it.trim().toFormat(tt) }
				}
			}
			else -> null
		}
		
		// Конвертация из Number
		this is Number -> when (targetClass) {
			Byte::class -> toByte()
			Short::class -> toShort()
			Int::class -> toInt()
			Long::class -> toLong()
			Float::class -> toFloat()
			Double::class -> toDouble()
			LocalDate::class -> LocalDate.ofEpochDay(toLong() / 1000)
			LocalDateTime::class -> LocalDateTime.ofEpochSecond(toLong() / 1000, 0, ZoneOffset.UTC)
			Char::class -> toInt().toChar()
			String::class -> toString()
			Boolean::class -> toInt() != 0
			else -> null
		}
		
		// Конвертация из Boolean
		this is Boolean -> when (targetClass) {
			Int::class, Number::class -> if (this) 1 else 0
			Long::class -> if (this) 1L else 0L
			Float::class -> if (this) 1f else 0f
			Double::class -> if (this) 1.0 else 0.0
			String::class -> toString()
			else -> null
		}
		
		this is Enum<*> -> when (targetClass) {
			String::class -> toString()
			Int::class, Number::class -> ordinal
			Long::class -> ordinal.toLong()
			Float::class -> ordinal.toFloat()
			Double::class -> ordinal.toDouble()
			else -> null
		}
		
		else -> null
	}
}
fun Any?.toFormatSafe(type: KType) = this?.toFormat(type)
inline fun <reified Type : Any> Any?.toFormatSafeAs(): Type? = this?.toFormatAs<Type>()

// Вспомогательные методы парсинга
inline fun <reified T : Number> String.tryParseNumber(
	converter: String.() -> T
): T? = try {
	replace("[fFdDlLbBsS]".toRegex(), "").let {
		when {
			contains(".") -> it.toDouble().let { num ->
				when (T::class) {
					Byte::class -> num.toInt().toByte()
					Short::class -> num.toInt().toShort()
					Int::class -> num.toInt()
					Long::class -> num.toLong()
					Float::class -> num.toFloat()
					Double::class -> num
					else -> it.converter()
				} as T
			}
			else -> it.converter()
		}
	}
} catch (e: Exception) { null }
fun String.tryParseByte() = tryParseNumber<Byte> { toByte() }
fun String.tryParseShort() = tryParseNumber<Short> { toShort() }
fun String.tryParseInt() = tryParseNumber<Int> { toInt() }
fun String.tryParseLong() = tryParseNumber<Long> { toLong() }
fun String.tryParseFloat() = tryParseNumber<Float> { toFloat() }
fun String.tryParseDouble() = tryParseNumber<Double> { toDouble() }
fun String.tryParseLocalDate(
	vararg patterns: String = arrayOf(
		"yyyy-MM-dd",
		"dd.MM.yyyy",
		"MM/dd/yyyy"
	)
): LocalDate? = patterns.firstNotNullOfOrNull { pattern ->
	try {
		LocalDate.parse(this, DateTimeFormatter.ofPattern(pattern))
	} catch (e: Exception) { null }
}
fun String.tryParseLocalDateTime(
	vararg patterns: String = arrayOf(
		"yyyy-MM-dd HH:mm:ss",
		"dd.MM.yyyy HH:mm:ss",
		"MM/dd/yyyy HH:mm:ss"
	)
): LocalDateTime? = patterns.firstNotNullOfOrNull { pattern ->
	try {
		LocalDateTime.parse(this, DateTimeFormatter.ofPattern(pattern))
	} catch (e: Exception) { null }
}
fun String.tryParseBoolean(): Boolean? = when {
	equals("true", ignoreCase = true) -> true
	equals("false", ignoreCase = true) -> false
	tryParseDouble() != null -> tryParseDouble()?.let { it != 0.0 }
	else -> null
}

inline fun <reified Type : Any> getTypeArguments(): List<KType> {
	val types = mutableListOf<KType>()
	val type = typeOf<Type>()
	
	// Получаем аргументы типа
	val typeArguments = type.arguments
	
	// Добавляем все аргументы типа в список
	typeArguments.forEach { arg ->
		arg.type?.let { types.add(it) }
	}
	
	return types
}

fun KType.getTypeArguments(): List<KType> {
	val types = mutableListOf<KType>()
	
	arguments.forEach { arg ->
		arg.type?.let { types.add(it) }
	}
	
	return types
}