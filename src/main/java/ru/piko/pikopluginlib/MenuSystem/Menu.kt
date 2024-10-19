package ru.piko.pikopluginlib.MenuSystem

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import ru.piko.pikopluginlib.Items.ItemBuilder
import ru.piko.pikopluginlib.PlayersData.PlayerData
import ru.piko.pikopluginlib.Utils.toComponent

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
	
	protected val FILLER_GLASS: ItemStack =
		ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").setCustomModelData(190).build()
	protected val FILLER_LIGHT_GLASS: ItemStack =
		ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE).setDisplayName(" ").setCustomModelData(190).build()
	protected val ARROW_BACK: ItemStack =
		ItemBuilder(Material.ARROW).setDisplayName("&cНазад").setCustomModelData(98).build()
	protected val BARRIER_CLOSE: ItemStack =
		ItemBuilder(Material.BARRIER).setDisplayName("&cЗакрыть").setCustomModelData(99).build()
	
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
		if (!isOpening) return
		
		inventory = Bukkit.createInventory(this, getSlots(), getMenuName())
		
		setMenuItems()
		
		// Создаем и вызываем событие PlayerOpenMenuEvent
		val event = PlayerOpenMenuEvent(playerData.owner, this)
		Bukkit.getPluginManager().callEvent(event)
		
		// Проверяем, не было ли событие отменено
		if (!event.isCancelled) {
			playerData.owner.openInventory(inventory)
		}
	}
	
	protected fun isInventoryInitialized(): Boolean {
		return _inventory != null
	}
	
	fun setFillerGlass() {
		setFillerItem(FILLER_GLASS)
	}
	
	fun setFillerLightGlass() {
		setFillerItem(FILLER_LIGHT_GLASS)
	}
	
	fun setFillerItem(item: ItemStack) {
		for (i in 0 until getSlots()) {
			if (inventory.getItem(i) == null) {
				inventory.setItem(i, item)
			}
		}
	}
	
	fun setItem(slot: Int, item: ItemStack) {
		inventory.setItem(slot, item)
	}
}