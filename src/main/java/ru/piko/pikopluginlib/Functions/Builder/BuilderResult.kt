package ru.piko.pikopluginlib.Functions.Builder

import ru.piko.pikopluginlib.Functions.ChainResult

/**
 * @param action - Тип действия
 * @param valueInt - Числовое значение используется для Step и Position
 * @param valueString - Строка пока используется только для Error
 * @param valueAny - Любой тип, может пригодиться
 * @param force Принудительный запуск. Без ожидания следующей итерации. Используется при Step и Position
 * @param breek Это break. Полностью завершает цепочку
 */
data class BuilderResult(
	val action: ActionType,
	val valueInt: Int = 0,
	val valueString: String = "",
	val valueAny: Any? = null,
	val force: Boolean = false,
	val breek: Boolean = false,
) {
	companion object Static {
		val None = BuilderResult(ActionType.None, 0)                          // Ничего, следующая итерация на этот же элемент
		val Break = BuilderResult(ActionType.None, 0, breek = true)           // Полностью заканчивает цепочку
		val Next = BuilderResult(ActionType.Step, 1)                          // Следующий элемент
		val Back = BuilderResult(ActionType.Step, -1)                         // Предыдущий элемент
		val Again = BuilderResult(ActionType.Position, 0)                     // Запускает с первого элемента
		val Continue = BuilderResult(ActionType.Step, 1, force = true)        // Сразу же исполняет следующий элемент
		val Previous = BuilderResult(ActionType.Step, -1, force = true)       // Сразу же исполняет предыдущий элемент
		val AgainForce = BuilderResult(ActionType.Position, 0, force = true)  // Сразу же запускает с первого элемента
		
		// Скачки на 2 позиции
		val Next2 = BuilderResult(ActionType.Step, 2)                         // Скачок на 2 элемента вперед
		val Back2 = BuilderResult(ActionType.Step, -2)                        // Скачок на 2 элемента назад
		val Continue2 = BuilderResult(ActionType.Step, 2, force = true)       // Сразу же исполняет элемент через один
		val Previous2 = BuilderResult(ActionType.Step, -2, force = true)      // Сразу же исполняет элемент за два до текущего
	}
	
	fun toChainResult(): ChainResult? {
		return when (action) {
			ActionType.None -> if (breek) ChainResult.Break else ChainResult.None
			ActionType.Step -> when {
				force -> when (valueInt) {
					1 -> ChainResult.Continue
					2 -> ChainResult.Continue2
					-1 -> ChainResult.Previous
					-2 -> ChainResult.Previous2
					else -> null
				}
				else -> when (valueInt) {
					1 -> ChainResult.Next
					2 -> ChainResult.Next2
					-1 -> ChainResult.Back
					-2 -> ChainResult.Back2
					else -> null
				}
			}
			ActionType.Position -> if (force) ChainResult.AgainForce else ChainResult.Again
			ActionType.Error -> null
		}
	}
}