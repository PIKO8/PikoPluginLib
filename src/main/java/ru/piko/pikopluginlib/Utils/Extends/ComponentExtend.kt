package ru.piko.pikopluginlib.Utils.Extends

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

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