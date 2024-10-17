@file:Suppress("PackageName", "unused")

package ru.piko.pikopluginlib.Functions

import org.bukkit.plugin.java.JavaPlugin
import ru.piko.pikopluginlib.Utils.Edit
import ru.piko.pikopluginlib.Utils.MadeAI
import ru.piko.pikopluginlib.Utils.NotRecommended

/**
 * Класс для построения цепочки функций. <br>
 *
 *  Контейнер `data` который передаётся в каждую функцию.
 *
 *  Константы которые там хранятся:
 *
 * `ITERATION` - Считает КАЖДЫЙ запуск `run`
 *
 * `REPEATS` - MutableMap<Int, Int> которая хранит repeats
 *
 * `PREVIOUS_RESULT` - Результат предыдущей функции
 *
 * `INDEX` - Текущий индекс в списке
 *
 * `SIZE` - Текущий размер списка
 *
 * `ADD_FUNCTION` - Принимает `List<(MutableMap<String, Any>) -> BuilderResult>` или `(MutableMap<String, Any>) -> BuilderResult`.
 *  Эти функции(я) добавляться в конце итерации в конец function.
 *
 * `SET_FUNCTION` - Принимает `Map<Int, (MutableMap<String, Any>) -> BuilderResult>` или `Pair<Int, (MutableMap<String, Any>) -> BuilderResult>`.
 *  Эти функции(я) добавляться в конце итерации по нужным индексам.
 *
 * `REMOVE_FUNCTION` - Принимает `List<Int>` или `Int`.
 *  Функции по этим индексам удаляться в конце итерации.  (Приоритетней чем `ADD_FUNCTION`)
 */
@MadeAI(ai = Edit.Medium, human = Edit.Medium)
class FunctionBuilder private constructor(
	plugin: JavaPlugin,
	ticks: Long,
	delay: Long = 0,
	id: String,
	stopAllWithId: Boolean,
) : FunctionAbstract(plugin, ticks, delay, id, stopAllWithId) {
	private val functions = mutableListOf<(MutableMap<String, Any>) -> BuilderResult>()
	private val data = mutableMapOf(
		Pair(ITERATION, 0),
		Pair(REPEATS, mutableListOf<RepeatData>()),
		Pair(PREVIOUS_RESULT, BuilderResult.None),
		Pair(INDEX, 0),
	)
	
	@Suppress("UNCHECKED_CAST")
	@MadeAI(ai = Edit.Many, human = Edit.Minimum)
	override fun run() {
		// Увеличиваем количество итераций
		data[ITERATION] = (data[ITERATION] as Int) + 1
		// Получаем текущий индекс
		val currentIndex = data[INDEX] as Int
		
		// Проверяем, есть ли функции для выполнения
		if (currentIndex < functions.size) {
			// Устанавливаем размер списка функций
			data[SIZE] = functions.size
			
			// Получаем текущую функцию
			val currentFunction = functions[currentIndex]
			
			// Вызываем функцию и получаем результат
			val result = currentFunction(data)
			
			// Обновляем предыдущий результат
			data[PREVIOUS_RESULT] = result
			
			// Обрабатываем результат
			when {
				result.breek -> {
					// Если breek равно true, уничтожаем цепочку
					destroySelf()
				}
				result.action == ActionType.None -> {
					// Ничего не делаем, просто продолжаем
				}
				result.action == ActionType.Step -> {
					// Увеличиваем индекс на значение, указанное в result.value
					val newIndex = currentIndex + result.valueInt
					
					// Обрабатываем новый индекс перед проверкой force
					data[INDEX] = when {
						newIndex < 0 -> 0 // На начало, если индекс отрицательный
						newIndex >= functions.size -> functions.size - 1 // На конец, если индекс выходит за пределы
						else -> newIndex // В пределах массива
					}
					
					// Если force равно true, выполняем следующую функцию немедленно
					if (result.force) {
						run() // Запускаем следующую итерацию немедленно
					}
				}
				result.action == ActionType.Position -> {
					// Устанавливаем индекс на значение, указанное в result.value
					val newIndex = result.valueInt
					
					// Обрабатываем новый индекс перед проверкой force
					data[INDEX] = when {
						newIndex < 0 -> 0 // На начало, если индекс отрицательный
						newIndex >= functions.size -> functions.size - 1 // На конец, если индекс выходит за пределы
						else -> newIndex // В пределах массива
					}
					
					// Если force равно true, выполняем функцию немедленно
					if (result.force) {
						run() // Запускаем следующую итерацию немедленно
					}
				}
				result.action == ActionType.Error -> {
					plugin.logger.warning("FunctionBuilder id: $id, ticks: $ticks, delay: $delay. destroy self because of a mistake")
					plugin.logger.warning("FunctionBuilder(from PikoPluginLib) error: ${result.valueString}")
					destroySelf()
				}
			}
			
			// Обработка функций для удаления
			when (val removeFunctions = data[REMOVE_FUNCTION]) {
				is List<*> -> {
					// Сортируем индексы в обратном порядке и удаляем
					val sortedIndices = removeFunctions
						.filterIsInstance<Int>() // Оставляем только Int
						.sortedByDescending { it } // Сортируем в обратном порядке
					for (index in sortedIndices) {
						if (index in functions.indices) {
							functions.removeAt(index)
						}
					}
				}
				is Int -> {
					if (removeFunctions in functions.indices) {
						functions.removeAt(removeFunctions)
					}
				}
			}
			data.remove(REMOVE_FUNCTION) // Удаляем ключ после обработки
			
			// Обработка функций для добавления
			when (val addFunctions = data[ADD_FUNCTION]) {
				is List<*> -> {
					// Фильтруем и добавляем только те функции, которые соответствуют типу
					val validFunctions = addFunctions.filterIsInstance<(MutableMap<String, Any>) -> BuilderResult>()
					functions.addAll(validFunctions)
				}
				else -> {
					// Если это не список, проверяем, является ли это функцией нужного типа
					if (addFunctions is Function<*>) {
						// Явное приведение типа, если уверены в типе
						val function = addFunctions as? (MutableMap<String, Any>) -> BuilderResult
						function?.let { functions.add(it) }
					}
				}
			}
			data.remove(ADD_FUNCTION) // Удаляем ключ после обработки
			
			// Обработка функций для установки
			when (val setFunctions = data[SET_FUNCTION]) {
				is Map<*, *> -> {
					for ((key, value) in setFunctions) {
						if (key is Int && value is Function<*>) {
							// Явное приведение типа
							val function = value as? (MutableMap<String, Any>) -> BuilderResult
							if (function != null && key in functions.indices) {
								functions[key] = function
							}
						}
					}
				}
				is Pair<*, *> -> {
					if (setFunctions.first is Int && setFunctions.second is Function<*>) {
						// Явное приведение типа
						val function = setFunctions.second as? (MutableMap<String, Any>) -> BuilderResult
						val index = setFunctions.first as Int
						if (function != null && index in functions.indices) {
							functions[index] = function
						}
					}
				}
			}
			data.remove(SET_FUNCTION) // Удаляем ключ после обработки
		} else {
			data[INDEX] = 0 // На начало, если вышли за пределы
		}
	}
	
	override fun destroySelf() {
		task?.cancel()
		list.remove(this)
	}
	
	override fun init() {
		list.add(this)
	}
	
	companion object {
		// <editor-fold defaultstate="collapsed" desc="List & create">
		val list: MutableList<FunctionBuilder> = ArrayList()
		
		/**
		 * Создает новый экземпляр FunctionBuilder.
		 *
		 * @param plugin Плагин, к которому относится цепочка функций.
		 * @param ticks Время между вызовами функций.
		 * @param delay Задержка перед первым вызовом функции.
		 * @param id Идентификатор цепочки функций.
		 * @param stopAllWithId Если true, уничтожает все цепочки с тем же id.
		 * @return Новый экземпляр FunctionBuilder.
		 */
		fun create(plugin: JavaPlugin, ticks: Long, delay: Long = 0, id: String = "", stopAllWithId: Boolean = false): FunctionBuilder {
			return FunctionBuilder(plugin, ticks, delay, id, stopAllWithId)
		}
		// </editor-fold>
		
		// <editor-fold defaultstate="collapsed" desc="Удаление / Destroys">
		fun destroy(builder: FunctionBuilder) {
			builder.destroySelf()
		}
		
		@Suppress("DeprecatedCallableAddReplaceWith")
		@NotRecommended("Может сломать что-нибудь в других плагинах лучше использовать destroyAll(plugin: JavaPlugin, id: String)")
		@Deprecated("Не рекомендованный")
		fun destroyAll(id: String) {
			list.forEach {
				if (it.id == id) {
					it.destroySelf()
				}
			}
		}
		
		fun destroyAll(plugin: JavaPlugin, id: String) {
			list.forEach {
				if (it.plugin == plugin && it.id == id) {
					it.destroySelf()
				}
			}
		}
		// </editor-fold>
		
		// <editor-fold defaultstate="collapsed" desc="Константы / Const">
		/** is `MutableMap<Int, RepeatData>` */
		const val REPEATS = "repeats"
		/** is `Int` */
		const val ITERATION = "iteration"
		/** is `BuilderResult` */
		const val PREVIOUS_RESULT = "previousResult"
		/** is `Int` */
		const val INDEX = "index"
		/** is `Int` */
		const val SIZE = "size"
		const val ADD_FUNCTION = "addFunction"
		const val SET_FUNCTION = "setFunction"
		const val REMOVE_FUNCTION = "removeFunction"
		// </editor-fold>
		
		data class RepeatData(
			var count: Int = 0,
			val maxRepeats: Int,
			val stepTrue: Int,
			val stepFalse: Int
		)
	}
	// ----------------------------------------
	
	// <editor-fold defaultstate="collapsed" desc="Условия / Conditions">
	/**
	 * Добавляет условие, будет ожидаться пока оно не выполниться
	 *
	 * @param stepTrue  Шагов при `true`. По умолчанию 1
	 * @param condition Условие, возвращающее true или false.
	 * @return Текущий экземпляр FunctionBuilder для дальнейшего построения.
	 */
	fun conditionWhile(stepTrue: Int = 1, condition: (MutableMap<String, Any>) -> Boolean): FunctionBuilder {
		functions.add { if (condition(it)) BuilderResult(ActionType.Step, stepTrue) else BuilderResult.None }
		return this
	}
	
	/**
	 * Добавляет условие, если `false` то перепрыгивает на количество функций указанных в `step`
	 *
	 * @param stepFalse Шагов при `false`. По умолчанию 2
	 * @param stepTrue  Шагов при `true`. По умолчанию 1
	 * @param condition Условие, возвращающее true или false.
	 * @return Текущий экземпляр FunctionBuilder для дальнейшего построения.
	 */
	fun conditionSkip(stepFalse: Int = 2, stepTrue: Int = 1, condition: (MutableMap<String, Any>) -> Boolean): FunctionBuilder {
		functions.add {
			if (condition(it)) BuilderResult(ActionType.Step, stepTrue) else BuilderResult(
				ActionType.Step,
				stepFalse
			)
		}
		return this
	}
	// </editor-fold>
	
	// ----------------------------------------
	
	// <editor-fold defaultstate="collapsed" desc="Ограничение">
	/**
	 * Добавляет ограничение на количество выполнений для блока функций.
	 *
	 * @param maxRepeats Максимальное количество повторений.
	 * @param stepFalse Количество шагов после достижения лимита повторений.
	 * @param stepTrue Количество шагов при каждом повторении в пределах лимита.
	 * @return Текущий экземпляр FunctionBuilder для дальнейшего построения.
	 */
	@Suppress("UNCHECKED_CAST")
	fun repeats(maxRepeats: Int, stepFalse: Int = 2, stepTrue: Int = 1): FunctionBuilder {
		val repeatsListInData = data[REPEATS] as? MutableList<RepeatData>
			?: mutableListOf<RepeatData>().also { data[REPEATS] = it }
		
		val currentIndex = repeatsListInData.size
		repeatsListInData.add(RepeatData(maxRepeats = maxRepeats, stepTrue = stepTrue, stepFalse = stepFalse))
		
		functions.add { map ->
			val repeatsListInner = map[REPEATS] as? MutableList<RepeatData>
				?: return@add BuilderResult(ActionType.Error, valueString = "REPEATS list is missing or has incorrect type")
			
			val repeatData = repeatsListInner.getOrNull(currentIndex)
				?: return@add BuilderResult(ActionType.Error, valueString = "Repeat data is missing for current index")
			
			if (repeatData.count < repeatData.maxRepeats) {
				repeatData.count++
				BuilderResult(ActionType.Step, repeatData.stepTrue)
			} else {
				BuilderResult(ActionType.Step, repeatData.stepFalse)
			}
		}
		return this
	}
	// </editor-fold>
	
	// ----------------------------------------
	
	// <editor-fold defaultstate="collapsed" desc="Функции / Functions">
	/**
	 * Добавляет функцию, которая выполняет заданный блок кода.
	 *
	 * @param function Блок кода, который будет выполнен.
	 * @return Текущий экземпляр FunctionBuilder для дальнейшего построения.
	 */
	fun functionUnit(function: (MutableMap<String, Any>) -> Unit): FunctionBuilder {
		functions.add {
			function(it)
			BuilderResult.Next
		}
		return this
	}
	
	/**
	 * Добавляет функцию, которая возвращает булево значение.
	 *
	 * `true` сразу запустит следующую функцию.
	 *
	 * `false` следующая функция на следующей итерации.
	 * @param function Функция, возвращающая true или false.
	 * @return Текущий экземпляр FunctionBuilder для дальнейшего построения.
	 */
	fun functionBoolean(function: (MutableMap<String, Any>) -> Boolean): FunctionBuilder {
		functions.add {
			if (function(it)) BuilderResult.Continue else BuilderResult.Next
		}
		return this
	}
	
	/**
	 * Добавляет функцию, которая возвращает результат билдера.
	 *
	 * @param function Функция, возвращающая BuilderResult
	 * @return Текущий экземпляр FunctionBuilder для дальнейшего построения.
	 */
	fun function(function: (MutableMap<String, Any>) -> BuilderResult): FunctionBuilder {
		functions.add(function)
		return this
	}
	
	/**
	 * Добавляет список функций в цепочку, которые не возвращают значение.
	 *
	 * @param functions Список функций, которые будут добавлены.
	 * @return Текущий экземпляр FunctionBuilder для дальнейшего построения.
	 */
	fun functionsUnit(functions: List<(MutableMap<String, Any>) -> Unit>): FunctionBuilder {
		this.functions.addAll(functions.map { f ->
			{ context: MutableMap<String, Any> ->
				f(context)
				BuilderResult.Next
			}
		})
		return this
	}
	
	/**
	 * Добавляет список функций в цепочку, которые возвращают булево значение.
	 *
	 * `true` сразу запустит следующую функцию.
	 * `false` следующая функция на следующей итерации.
	 *
	 * @param functions Список функций, возвращающих true или false.
	 * @return Текущий экземпляр FunctionBuilder для дальнейшего построения.
	 */
	fun functionsBoolean(functions: List<(MutableMap<String, Any>) -> Boolean>): FunctionBuilder {
		this.functions.addAll(functions.map { f ->
			{ context: MutableMap<String, Any> ->
				if (f(context)) BuilderResult.Continue else BuilderResult.Next
			}
		})
		return this
	}
	
	/**
	 * Добавляет список функций в цепочку.
	 *
	 * @param functions Список функций, которые будут добавлены.
	 * @return Текущий экземпляр FunctionBuilder для дальнейшего построения.
	 */
	fun functions(functions: List<(MutableMap<String, Any>) -> BuilderResult>): FunctionBuilder {
		this.functions.addAll(functions)
		return this
	}
	// </editor-fold>
	
	fun invoke() {
		initFunction()
	}
}