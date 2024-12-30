package ru.piko.pikopluginlib.Listeners

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import ru.piko.pikopluginlib.MenuSystem.Menu

class MenuListener : Listener {
	@EventHandler
	fun onClickMenu(event: InventoryClickEvent) {
		val holder = event.clickedInventory?.holder ?: return
		if (holder !is Menu) return
		
		val player = event.whoClicked as Player
		
		event.isCancelled = true
		
		val item = event.currentItem
		
		if (item != null && item.itemMeta != null && item.itemMeta.hasCustomModelData()) {
			//val playerData: PlayerData = main.api.playerData.get(player.uniqueId)
			when (item.itemMeta.customModelData) {
				99 -> player.closeInventory()
			}
		}
		holder.clickMenu(event)
	}
	
	@EventHandler
	fun onCloseMenu(event: InventoryCloseEvent) {
		val menu = event.inventory.holder
		if (menu is Menu) {
			menu.closeMenu(event)
		}
	}
}