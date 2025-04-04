package ru.piko.pikopluginlib.MenuSystem.Events

import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent
import ru.piko.pikopluginlib.MenuSystem.Menu

class PlayerOpenMenuEvent(player: Player, val menu: Menu) : PlayerEvent(player), Cancellable {
	private var isCancelled = false
	
	override fun getHandlers(): HandlerList {
		return handlerList
	}
	
	override fun isCancelled(): Boolean {
		return isCancelled
	}
	
	override fun setCancelled(cancel: Boolean) {
		isCancelled = cancel
	}
	
	companion object {
		@JvmStatic
		val handlerList = HandlerList()
	}
}