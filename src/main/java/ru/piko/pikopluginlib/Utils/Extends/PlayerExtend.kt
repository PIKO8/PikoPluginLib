package ru.piko.pikopluginlib.Utils.Extends

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

fun Player.addItemSafe(item: ItemStack) {
	// Проверяем, что предмет не пустой
	if (item.type == Material.AIR) {
		return
	}
	
	// Получаем инвентарь игрока
	val inventory = this.inventory
	
	// Пытаемся добавить предмет в инвентарь
	val addedItems = inventory.addItem(item)
	
	// Если предмет не был добавлен, выбрасываем его рядом с игроком
	if (addedItems.isNotEmpty()) {
		// Выбрасываем каждый предмет, который не удалось добавить
		for (remainingItem in addedItems.values) {
			this.world.dropItem(this.location.add(Vector(0.5, 0.5, 0.5)), remainingItem)
		}
	}
}

fun Player.addItemsSafe(items: Collection<ItemStack>) {
	items.forEach { this.addItemSafe(it) }
}

fun Player.addItemsSafe(vararg items: ItemStack) {
	items.forEach { this.addItemSafe(it) }
}