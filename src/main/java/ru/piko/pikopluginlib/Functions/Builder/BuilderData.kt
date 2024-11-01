@file:Suppress("UNCHECKED_CAST")

package ru.piko.pikopluginlib.Functions.Builder

/**
 *  Класс, который хранит данные между функциями <br>
 *
 *  Константы которые там хранятся. Им нельзя менять тип:
 *
 * `iteration` - Считает КАЖДЫЙ запуск
 *
 * `repeats` - `List<RepeatData>` которая хранит данные для repeats
 *
 * `previous_result` - Результат предыдущей функции
 *
 * `index` - Текущий индекс в списке функций
 *
 * `sizeFunctions` - Текущий размер списка функций
 *
 * `functions` - Список функций, is List<BuilderFunction>
 */
class BuilderData : MutableMap<String, Any> {
	private val internalMap: MutableMap<String, Any> = mutableMapOf(
		Pair(ITERATION, 0),
		Pair(REPEATS, mutableListOf<RepeatData>()),
		Pair(COUNTERS, mutableListOf<CounterData>()),
		Pair(PREVIOUS_RESULT, BuilderResult.None),
		Pair(INDEX, 0),
		Pair(FUNCTIONS, mutableListOf<BuilderFunction>())
	)
	private val lockedKeys = setOf(ITERATION, REPEATS, PREVIOUS_RESULT, INDEX, SIZE, FUNCTIONS)
	
	override operator fun get(key: String): Any? {
		return internalMap[key]
	}
	
	inline fun <reified T> getAs(key: String): T? {
		return get(key) as? T
	}
	
	override fun remove(key: String): Any? {
		return if (key !in lockedKeys) internalMap.remove(key) else null
	}
	
	// Метод для изменения значения по ключу с проверкой типа
	operator fun set(key: String, value: Any) {
		val any = internalMap[key]
		if (key !in lockedKeys || any == null || any::class == value::class) {
			internalMap[key] = value // Прямое изменение значения
		}
	}
	
	// Переопределяем put, чтобы он использовал нашу логику set
	override fun put(key: String, value: Any): Any? {
		val oldValue = internalMap[key]
		set(key, value)
		return oldValue
	}
	
	// <editor-fold defaultstate="collapsed" desc="data const">
	  // <editor-fold-sub defaultstate="collapsed" desc="iteration">
	// Методы для доступа через точечную нотацию с проверками типов
	var iteration: Int
		get() = this[ITERATION] as Int
		set(value) {
			this[ITERATION] = value // Прямое изменение значения
		}
	  // </editor-fold-sub>
	
		// <editor-fold-sub defaultstate="collapsed" desc="counters">
	var counters: MutableList<CounterData>
		get() = this[COUNTERS] as MutableList<CounterData>
		set(value) {
			this[COUNTERS] = value
		}
		// </editor-fold-sub>
	
	  // <editor-fold-sub defaultstate="collapsed" desc="repeats">
	var repeats: MutableList<RepeatData>
		get() = this[REPEATS] as MutableList<RepeatData>
		set(value) {
			this[REPEATS] = value // Прямое изменение значения
		}
	  // </editor-fold-sub>
	
	  // <editor-fold-sub defaultstate="collapsed" desc="previousResult">
	var previousResult: BuilderResult
		get() = this[PREVIOUS_RESULT] as BuilderResult
		set(value) {
			this[PREVIOUS_RESULT] = value // Прямое изменение значения
		}
	  // </editor-fold-sub>
	
	  // <editor-fold-sub defaultstate="collapsed" desc="index">
	var index: Int
		get() = this[INDEX] as Int
		set(value) {
			this[INDEX] = value // Прямое изменение значения
		}
	  // </editor-fold-sub>
	
	  // <editor-fold-sub defaultstate="collapsed" desc="sizeFunctions">
	var sizeFunctions: Int
		get() = this[SIZE] as Int
		set(value) {
			this[SIZE] = value // Прямое изменение значения
		}
	  // </editor-fold-sub>
	
	  // <editor-fold-sub defaultstate="collapsed" desc="functions">
	var functions: MutableList<BuilderFunction>
		get() = this[FUNCTIONS] as MutableList<BuilderFunction>
		set(value) {
			this[FUNCTIONS] = value // Прямое изменение значения
		}
	  // </editor-fold-sub>
	
	// </editor-fold>
	
	
	// <editor-fold defaultstate="collapsed" desc="Map override">
	override val entries: MutableSet<MutableMap.MutableEntry<String, Any>>
		get() = internalMap.entries
	
	override val keys: MutableSet<String>
		get() = internalMap.keys
	
	override val size: Int
		get() = internalMap.size
	
	override val values: MutableCollection<Any>
		get() = internalMap.values
	
	override fun clear() {
		internalMap.keys.toList().forEach {
			if (it !in lockedKeys) internalMap.remove(it)
		}
	}
	
	override fun isEmpty(): Boolean {
		return false
	}
	
	override fun putAll(from: Map<out String, Any>) {
		from.forEach { (key, value) -> set(key, value) }
	}
	
	override fun containsValue(value: Any): Boolean = internalMap.containsValue(value)
	
	override fun containsKey(key: String): Boolean = internalMap.containsKey(key)
	// </editor-fold>
	
	companion object Static {
		// <editor-fold defaultstate="collapsed" desc="Константы / Const">
		/** is `List<RepeatData>` */
		const val REPEATS = "repeats"
		/** is `List<CounterData>` */
		const val COUNTERS = "counters"
		/** is `Int` */
		const val ITERATION = "iteration"
		/** is `BuilderResult` */
		const val PREVIOUS_RESULT = "previousResult"
		/** is `Int` */
		const val INDEX = "index"
		/** is `Int` */
		const val SIZE = "size"
		/** is `List<BuilderFunction>` */
		const val FUNCTIONS = "functions"
		// </editor-fold>
	}
}