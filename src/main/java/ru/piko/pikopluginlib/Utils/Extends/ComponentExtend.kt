package ru.piko.pikopluginlib.Utils.Extends

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

object ComponentExtend {
	public val mini = MiniMessage.miniMessage()
	
	public fun mini(value: String): Component {
		return mini.deserialize("<italic:false>$value")
	}
	
	public fun mini(vararg values: String): List<Component> {
		return values.map { mini(it) }
	}
}

public fun String.mini(): Component {
	return ComponentExtend.mini(this)
}

// Вспомогательная функция для преобразования строк с &-форматированием в Component
fun String.toComponent(): Component {
	return LegacyComponentSerializer.legacyAmpersand().deserialize(this)
}

fun Any.toComponent(): Component {
	return when (this) {
		is Component -> this
		is String -> this.toComponent()
		else -> LegacyComponentSerializer.legacyAmpersand().deserialize(this.toString())
	}
}

fun Any.toComponent(color: TextColor): Component {
	return this.toComponent().colorIfAbsent(color)
}