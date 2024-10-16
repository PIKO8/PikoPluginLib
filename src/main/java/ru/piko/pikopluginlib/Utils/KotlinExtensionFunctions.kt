package ru.piko.pikopluginlib.Utils

import org.bukkit.inventory.ItemStack

fun ItemStack.update(newItemStack: ItemStack) {
    this.itemMeta = newItemStack.itemMeta?.clone() // Клонируем метаданные
    this.amount = newItemStack.amount
    this.type = newItemStack.type
}