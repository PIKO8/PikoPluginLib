package ru.piko.pikopluginlib.Utils.Extends

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

val mini = MiniMessage.miniMessage()

fun mini(value: String): Component {
	return mini.deserialize("<italic:false>$value")
}

fun mini(vararg values: String): List<Component> {
	return values.map { mini(it) }
}