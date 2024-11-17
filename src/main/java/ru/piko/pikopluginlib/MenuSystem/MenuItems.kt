package ru.piko.pikopluginlib.MenuSystem

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import ru.piko.pikopluginlib.Items.ItemBuilder

object MenuItems {
	
	val FILLER_GLASS: ItemStack =
		ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").setCustomModelData(190).build()
	val FILLER_LIGHT_GLASS: ItemStack =
		ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE).setDisplayName(" ").setCustomModelData(190).build()
	val ARROW_BACK: ItemStack =
		ItemBuilder(Material.ARROW).setDisplayName("&cНазад").setCustomModelData(98).build()
	val BARRIER_CLOSE: ItemStack =
		ItemBuilder(Material.BARRIER).setDisplayName("&cЗакрыть").setCustomModelData(99).build()
	
}