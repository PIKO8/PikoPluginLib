package ru.piko.pikopluginlib.Utils

import org.bukkit.inventory.ItemStack

object KotlinExtensionFunctions {
    fun ItemStack.update(newItemStack: ItemStack) {
        this.itemMeta = newItemStack.itemMeta?.clone() // Клонируем метаданные
        this.amount = newItemStack.amount
        this.type = newItemStack.type
    }
    
    /**
     * Удобная функция.
     *
     * Если это такой тип, то проверить условие иначе пропустить(true) *
     *
     * До:
     *
     * `(entity is Player && entity.gameMode in listOf(GameMode.SURVIVAL, GameMode.ADVENTURE)) || entity !is Player`
     *
     * После:
     *
     * `entity.checkCondition<Player> { it.gameMode in listOf(GameMode.SURVIVAL, GameMode.ADVENTURE) }`
     */
    inline fun <reified T : Any> Any?.checkCondition(condition: (T) -> Boolean): Boolean {
        return (this is T && condition(this)) || this !is T
    }
    
    infix fun <One : Any, Two : Any> One.equals(value: Two): Boolean {
        return this.equals(value)
    }
}

