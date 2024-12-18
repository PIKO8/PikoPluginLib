package ru.piko.pikopluginlib.MenuSystem

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import ru.piko.pikopluginlib.MenuSystem.Events.PlayerOpenMenuEvent
import ru.piko.pikopluginlib.PlayersData.PlayerData
import ru.piko.pikopluginlib.Extends.toComponent

abstract class Menu(protected val playerData: PlayerData) : InventoryHolder {
	
	private var _inventory: Inventory? = null
	
	override fun getInventory(): Inventory {
		return _inventory ?: throw UninitializedPropertyAccessException("Inventory has not been initialized")
	}
	
	@get:JvmName("getInventoryProperty")
	protected var inventory: Inventory
		get() = _inventory ?: throw UninitializedPropertyAccessException("Inventory has not been initialized")
		set(value) {
			_inventory = value
		}
	
	@get:JvmName("getSlotsProperty")
	val slots: Int
		get() = getSlots()
	
	val player: Player
		get() = playerData.owner
	
	open var filter: ItemStack = MenuItems.FILLER_LIGHT_GLASS
		get() = field
		set(value) {
			field = value
		}
	
	@Deprecated("use getMenuNameComponent")
	abstract fun getMenuName(): String
	open fun getMenuNameComponent(): Component {
		return getMenuName().toComponent()
	}
	
	abstract fun getSlots(): Int
	abstract fun setMenuItems()
	abstract fun clickMenu(e: InventoryClickEvent)
	abstract fun closeMenu(e: InventoryCloseEvent)
	
	/**
	 * Будет ли создаваться и открываться меню. <br>
	 * Можно добавить проверки на null переменных в меню
	 */
	protected open var isOpening = true
	
	fun open() {
		// Create the inventory
		inventory = Bukkit.createInventory(this, getSlots(), getMenuNameComponent())
		
		// Set the menu items
		setMenuItems()
		
		// Create the PlayerOpenMenuEvent
		val event = PlayerOpenMenuEvent(playerData.owner, this)
		
		// Set the event's cancellation state based on isOpening
		event.isCancelled = !isOpening
		
		// Call the event
		Bukkit.getPluginManager().callEvent(event)
		
		// Check if the event was canceled
		if (!event.isCancelled) {
			// If the event is not canceled, open the inventory for the player
			playerData.owner.openInventory(inventory)
		}/* else {
			// Optionally handle the case where the event is canceled
			playerData.owner.sendMessage("You cannot open this menu.")
		}*/
	}
	
	protected fun isInventoryInitialized(): Boolean {
		return _inventory != null
	}
	
	fun setFillerItem(item: ItemStack) {
		for (i in 0 until getSlots()) {
			if (inventory.getItem(i) == null) {
				inventory.setItem(i, item)
			}
		}
	}
	
	fun setItem(slot: Int, item: ItemStack?) {
		inventory.setItem(slot, item)
	}
}