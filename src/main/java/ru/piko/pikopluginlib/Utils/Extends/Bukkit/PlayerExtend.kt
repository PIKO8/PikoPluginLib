package ru.piko.pikopluginlib.Utils.Extends.Bukkit

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import ru.piko.pikopluginlib.MenuSystem.Menu
import ru.piko.pikopluginlib.PlayersData.PlayerData
import ru.piko.pikopluginlib.Utils.InternalObject.main

object PlayerExtend {
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
				this.world.dropItem(this.location, remainingItem)
			}
		}
	}
	
	fun Player.addItemsSafe(items: Collection<ItemStack>) {
		items.forEach { this.addItemSafe(it) }
	}
	
	fun Player.addItemsSafe(vararg items: ItemStack) {
		items.forEach { this.addItemSafe(it) }
	}
	
	val Player.data: PlayerData get() = main.api.playerData.get(this.uniqueId)
	
	fun Player.openMenu(function: (Player) -> Menu) {
		function.invoke(this).open()
	}
}