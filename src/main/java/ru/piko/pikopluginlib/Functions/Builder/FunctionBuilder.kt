@file:Suppress("PackageName", "unused", "UNCHECKED_CAST")

package ru.piko.pikopluginlib.Functions.Builder

import org.bukkit.plugin.java.JavaPlugin
import ru.piko.pikopluginlib.Functions.FunctionAbstract
import ru.piko.pikopluginlib.Utils.PikoAnnotation
import ru.piko.pikopluginlib.Utils.PikoAnnotation.MadeAI
import ru.piko.pikopluginlib.Utils.PikoAnnotation.Edit
import ru.piko.pikopluginlib.Utils.PikoAnnotation.NotRecommended
import java.util.*

typealias BuilderFunction = (BuilderData) -> BuilderResult
/**
 * Класс для построения цепочки функций. <br>
 *
 * Контейнер `data` который передаётся в каждую функцию. Смотри [BuilderData]
 */
@MadeAI(ai = Edit.Medium, human = Edit.Medium)
class FunctionBuilder private constructor(
	plugin: JavaPlugin,
	ticks: Long,
	delay: Long = 0,
	id: String,
	stopAllWithId: Boolean,
) : FunctionAbstract(plugin, ticks, delay, id, stopAllWithId) {
	private val data: BuilderData = BuilderData()
	
	@MadeAI(ai = Edit.Many, human = Edit.Minimum)
	override fun run() {
		// Увеличиваем количество итераций
		data.iteration += 1
		// Получаем текущий индекс
		val currentIndex = data.index
		val functions = getFunctions()
		// Проверяем, есть ли функции для выполнения
		if (currentIndex < functions.size) {
			// Устанавливаем размер списка функций
			data.sizeFunctions = functions.size
			// Получаем текущую функцию
			val currentFunction = getFunction(currentIndex) ?: run {
				plugin.logger.warning("FunctionBuilder(from PikoPluginLib) error: function(index: ${currentIndex}) is null")
				destroySelf()
				return
			}
			// Вызываем функцию и получаем результат
			val result = try {
				currentFunction(data)
			} catch (e: Exception) {
				plugin.logger.warning("[PikoPluginLib] (${this::class.java}) Перехвачена ошибка в функции с id='$id'. Функция остановлена. Ошибка:")
				e.printStackTrace()
				BuilderResult.Break
			}
			
			// Обновляем предыдущий результат
			data.previousResult = result
			
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
					data.index = when {
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
					data.index = when {
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
		} else {
			data.index = 0 // На начало, если вышли за пределы
		}
	}
	
	override fun destroySelf() {
		task?.let { if (!it.isCancelled) it.cancel() }
		FunctionAbstract.removeObjectInList(list, this)
	}
	override fun init() {
		list.add(this)
	}
	companion object {
		// <editor-fold defaultstate="collapsed" desc="List & create">
		val list: MutableList<FunctionBuilder> = Collections.synchronizedList(mutableListOf())
		
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
		
		@NotRecommended("Может сломать что-нибудь в других плагинах лучше использовать destroyAll(plugin: JavaPlugin, id: String)")
		@Deprecated("Не рекомендованный")
		fun destroyAll(id: String) {
			val iterator = list.iterator()
			while (iterator.hasNext()) {
				val item = iterator.next()
				if (item.id == id) {
					item.destroySelf()
					iterator.remove()
				}
			}
		}
		
		fun destroyAll(plugin: JavaPlugin, id: String) {
			val iterator = list.iterator()
			while (iterator.hasNext()) {
				val item = iterator.next()
				if (item.plugin == plugin && item.id == id) {
					item.destroySelf()
					iterator.remove()
				}
			}
		}
		// </editor-fold>
		
		// <editor-fold defaultstate="collapsed" desc="Функции / Function">
		/**
		 * @param index куда добавлять функцию, по умолчанию `-1` - в конец
		 */
		fun addFunction(data: BuilderData, index: Int = -1, function: BuilderFunction) {
			val functions = getFunctions(data) ?: mutableListOf()
			if (index == -1) {
				functions.add(function)
			} else if (index in 0..functions.size) {
				functions.add(index, function)
			}
			data.functions = functions
		}
		
		/**
		 * @param index куда добавлять функцию, по умолчанию `-1` - `INDEX` из data - текущая функция
		 */
		fun setFunction(data: BuilderData, index: Int = -1, function: BuilderFunction) {
			// Извлекаем список функций из данных
			val functions = getFunctions(data) ?: mutableListOf()
			
			// Определяем индекс, по которому нужно заменить элемент
			val i = if (index == -1) {
				data.index
			} else {
				index
			}
			
			// Проверяем, что индекс находится в пределах допустимого диапазона
			if (i in functions.indices) {
				functions[i] = function // Заменяем элемент по индексу
			}
			
			// Обновляем список функций в данных
			data.functions = functions
		}
		
		/**
		 * @param index куда добавлять функцию, по умолчанию `-1` - `INDEX` из data - текущая функция
		 */
		fun getFunction(data: BuilderData, index: Int = -1): BuilderFunction? {
			// Извлекаем список функций из данных
			val functions = getFunctions(data) ?: mutableListOf()
			
			// Определяем индекс, по которому нужно получить элемент
			val i = if (index == -1) {
				data.index
			} else {
				index
			}
			
			// Проверяем, что индекс находится в пределах допустимого диапазона
			return functions.getOrNull(i) // Возвращаем элемент по индексу
		}
		
		fun getFunctions(data: BuilderData): MutableList<BuilderFunction>? {
			val functions = data.functions as? MutableList<BuilderFunction>
				?: return null
			return functions
		}
		
		/**
		 * @param index куда добавлять функцию, по умолчанию `-1` - `INDEX` из data - текущая функция
		 */
		fun removeFunction(data: BuilderData, index: Int = -1) {
			val functions = getFunctions(data) ?: mutableListOf()
				?: return
			val i = if (index == -1) {
				data.index
			} else index
			if (i in functions.indices) {
				functions.removeAt(i)
			}
			data.functions = functions
		}
		// </editor-fold>
	}
	// ----------------------------------------
	
	// <editor-fold defaultstate="collapsed" desc="Условия / Conditions">
	/**
	 * Добавляет условие, будет ожидаться пока оно не выполниться
	 *
	 * @param stepTrue Какой будет `BuilderResult` при `true`. По умолчанию `BuilderResult.Next`
	 * @param condition Условие, возвращающее true или false.
	 * @return Текущий экземпляр FunctionBuilder для дальнейшего построения.
	 */
	fun conditionWhile(stepTrue: BuilderResult = BuilderResult.Next, condition: (BuilderData) -> Boolean): FunctionBuilder {
		addFunction { if (condition(it)) stepTrue else BuilderResult.None }
		return this
	}
	
	/**
	 * Добавляет условие, будет ожидаться пока оно не выполниться
	 *
	 * @param stepTrue  Шагов при `true`. По умолчанию 1
	 * @param condition Условие, возвращающее true или false.
	 * @return Текущий экземпляр FunctionBuilder для дальнейшего построения.
	 */
	fun conditionWhile(stepTrue: Int = 1, condition: (BuilderData) -> Boolean): FunctionBuilder {
		addFunction { if (condition(it)) BuilderResult(ActionType.Step, stepTrue) else BuilderResult.None }
		return this
	}
	
	/**
	 * Добавляет условие, если `false` то перепрыгивает на количество функций указанных в `step`
	 *
	 * @param stepFalse Какой будет `BuilderResult` при `false`. По умолчанию `BuilderResult.Next2`
	 * @param stepTrue Какой будет `BuilderResult` при `true`. По умолчанию `BuilderResult.Next`
	 * @param condition Условие, возвращающее true или false.
	 * @return Текущий экземпляр FunctionBuilder для дальнейшего построения.
	 */
	fun conditionSkip(stepFalse: BuilderResult = BuilderResult.Next2, stepTrue: BuilderResult = BuilderResult.Next, condition: (BuilderData) -> Boolean): FunctionBuilder {
		addFunction {
			if (condition(it)) stepTrue else stepFalse
		}
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
	fun conditionSkip(stepFalse: Int = 2, stepTrue: Int = 1, condition: (BuilderData) -> Boolean): FunctionBuilder {
		addFunction {
			if (condition(it)) BuilderResult(ActionType.Step, stepTrue) else BuilderResult(
				ActionType.Step,
				stepFalse
			)
		}
		return this
	}
	// </editor-fold>
	
	// ----------------------------------------
	
	// <editor-fold defaultstate="collapsed" desc="Ограничения">
	/**
	 * Добавляет ограничение на количество выполнений для блока функций.
	 *
	 * @param repeats Максимальное количество повторений.
	 * @param stepFalse Количество шагов после достижения лимита повторений.
	 * @param stepTrue Количество шагов при каждом повторении в пределах лимита.
	 * @return Текущий экземпляр FunctionBuilder для дальнейшего построения.
	 */
	fun repeats(repeats: Int, stepFalse: Int = 2, stepTrue: Int = 1): FunctionBuilder {
		return repeats(
			repeats = repeats,
			resultTrue = BuilderResult(ActionType.Step, stepTrue),
			resultFalse = BuilderResult(ActionType.Step, stepFalse)
		)
	}
	
	/**
	 * Добавляет ограничение на количество выполнений для блока функций с возможностью
	 * указания произвольных результатов для случаев достижения и недостижения лимита.
	 *
	 * @param repeats Максимальное количество повторений.
	 * @param resultFalse Результат выполнения после достижения лимита повторений.
	 * @param resultTrue Результат выполнения при каждом повторении в пределах лимита.
	 * @return Текущий экземпляр FunctionBuilder для дальнейшего построения.
	 */
	fun repeats(repeats: Int, resultFalse: BuilderResult, resultTrue: BuilderResult = BuilderResult.Next): FunctionBuilder {
		val repeatsListInData = data.repeats
		
		val currentIndex = repeatsListInData.size
		repeatsListInData.add(RepeatData(maxRepeats = repeats, resultTrue = resultTrue, resultFalse = resultFalse))
		
		addFunction { map ->
			val repeatsListInner = map.repeats
			
			val repeatData = repeatsListInner.getOrNull(currentIndex)
				?: return@addFunction BuilderResult(ActionType.Error, valueString = "Repeat data is missing for current index")
			
			if (repeatData.count < repeatData.maxRepeats) {
				repeatData.count++
				return@addFunction repeatData.resultTrue
			} else {
				return@addFunction repeatData.resultFalse
			}
		}
		return this
	}
	
	
	/**
	 * Добавляет счетчик, который будет возвращать true каждые n значений
	 *
	 * @param every Через сколько значений возвращать true
	 * @param resultTrue Результат при достижении every
	 * @param resultFalse Результат при недостижении every
	 */
	fun counter(every: Int, resultTrue: BuilderResult, resultFalse: BuilderResult = BuilderResult.Next2): FunctionBuilder {
		val countersListInData = data.counters
		
		val currentIndex = countersListInData.size
		countersListInData.add(CounterData(every = every, resultTrue = resultTrue, resultFalse = resultFalse))
		
		addFunction { map ->
			val countersListInner = map.counters
			
			val counterData = countersListInner.getOrNull(currentIndex)
				?: return@addFunction BuilderResult(ActionType.Error, valueString = "Counter data is missing for current index")
			
			counterData.current++
			
			if (counterData.current >= counterData.every) {
				counterData.current = 0
				return@addFunction counterData.resultTrue
			} else {
				return@addFunction counterData.resultFalse
			}
		}
		return this
	}
	
	/**
	 * Добавляет счетчик, который будет возвращать true каждые n значений
	 *
	 * @param every Через сколько значений возвращать true
	 * @param stepTrue Количество шагов при достижении every
	 * @param stepFalse Количество шагов при недостижении every
	 */
	fun counter(every: Int, stepTrue: Int = 1, stepFalse: Int = 2): FunctionBuilder {
		return counter(
			every = every,
			resultTrue = BuilderResult(ActionType.Step, stepTrue),
			resultFalse = BuilderResult(ActionType.Step, stepFalse)
		)
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
	fun functionUnit(function: (BuilderData) -> Unit): FunctionBuilder {
		addFunction {
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
	fun functionBoolean(function: (BuilderData) -> Boolean): FunctionBuilder {
		addFunction {
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
	fun function(function: BuilderFunction): FunctionBuilder {
		addFunction(function = function)
		return this
	}
	
	/**
	 * Добавляет список функций в цепочку, которые не возвращают значение.
	 *
	 * @param functions Список функций, которые будут добавлены.
	 * @return Текущий экземпляр FunctionBuilder для дальнейшего построения.
	 */
	fun functionsUnit(functions: List<(BuilderData) -> Unit>): FunctionBuilder {
		functions.forEach { f ->
			addFunction { context: BuilderData ->
				f(context)
				BuilderResult.Next
			}
		}
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
	fun functionsBoolean(functions: List<(BuilderData) -> Boolean>): FunctionBuilder {
		functions.forEach { f ->
			addFunction { context: BuilderData ->
				if (f(context)) BuilderResult.Continue else BuilderResult.Next
			}
		}
		return this
	}
	
	/**
	 * Добавляет список функций в цепочку.
	 *
	 * @param functions Список функций, которые будут добавлены.
	 * @return Текущий экземпляр FunctionBuilder для дальнейшего построения.
	 */
	fun functions(functions: List<BuilderFunction>): FunctionBuilder {
		functions.forEach { addFunction(function = it) }
		return this
	}
	// </editor-fold>
	
	// <editor-fold defaultstate="collapsed" desc="Private Функции / Function">
	/**
	 * @param index куда добавлять функцию, по умолчанию `-1` - в конец
	 */
	fun addFunction(index: Int = -1, function: BuilderFunction) {
		val functions = getFunctions()
		if (index == -1) {
			functions.add(function)
		} else if (index in 0..functions.size) {
			functions.add(index, function)
		}
		data.functions = functions
	}
	
	fun setFunction(index: Int = -1, function: BuilderFunction) {
		val functions = getFunctions()
		
		// Определяем индекс для замены
		val i = if (index == -1) {
			data.index
		} else {
			index
		}
		
		// Проверяем, что индекс находится в пределах допустимого диапазона
		if (i in functions.indices) {
			functions[i] = function // Заменяем элемент по индексу
		}
		
		data.functions = functions
	}
	
	fun getFunction(index: Int = -1): (BuilderFunction)? {
		val functions = getFunctions()
		
		// Определяем индекс для получения
		val i = if (index == -1) {
			data.index
		} else {
			index
		}
		
		// Проверяем, что индекс находится в пределах допустимого диапазона
		return functions.getOrNull(i) // Возвращаем элемент по индексу
	}
	
	fun getFunctions(): MutableList<BuilderFunction> {
		val functions = data.functions
		return functions
	}
	
	fun removeFunction(index: Int = -1) {
		val functions = getFunctions()
		// Определяем индекс для удаления
		val i = if (index == -1) {
			data.index
		} else {
			index
		}
		
		// Проверяем, что индекс находится в пределах допустимого диапазона
		if (i in functions.indices) {
			functions.removeAt(i) // Удаляем элемент по индексу
		}
		data.functions = functions
	}
	// </editor-fold>
	
	fun invoke() {
		initFunction()
	}
}