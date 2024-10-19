package ru.piko.pikopluginlib.Functions.Builder

enum class ActionType {
	None,     // Пустышка
	Step,     // Шаг куда-нибудь
	Position, // Перемещение на позицию в списке
	Error,    // Ошибка!
}