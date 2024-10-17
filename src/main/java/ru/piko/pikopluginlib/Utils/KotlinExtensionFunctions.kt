package ru.piko.pikopluginlib.Utils

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

fun Player.openMenu(function: (Player) -> Menu): Menu {
    return function.invoke(this)
}