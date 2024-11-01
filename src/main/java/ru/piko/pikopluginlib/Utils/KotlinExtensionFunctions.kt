package ru.piko.pikopluginlib.Utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import ru.piko.pikopluginlib.Main
import ru.piko.pikopluginlib.MenuSystem.Menu
import ru.piko.pikopluginlib.PlayersData.PlayerData

fun ItemStack.update(newItemStack: ItemStack) {
    this.itemMeta = newItemStack.itemMeta?.clone() // Клонируем метаданные
    this.amount = newItemStack.amount
    this.type = newItemStack.type
}

fun Player.getData(): PlayerData {
    return Main.getPlugin().getPlayerData(this.uniqueId)
}

fun Player.openMenu(function: (Player) -> Menu) {
    function.invoke(this).open()
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

/** Например, до и после:
 * (entity is Player && entity.gameMode in listOf(GameMode.SURVIVAL, GameMode.ADVENTURE)) || entity !is Player
 * entity.checkCondition<Player> { it.gameMode in listOf(GameMode.SURVIVAL, GameMode.ADVENTURE) }
 * Если это такой тип то проверить условие иначе пропустить(true)
 */
inline fun <reified T : Any> Any?.checkCondition(condition: (T) -> Boolean): Boolean {
    return (this is T && condition(this)) || this !is T
}

infix fun <One : Any, Two : Any> One.equals(value: Two): Boolean {
    return this.equals(value)
}