package ru.piko.pikopluginlib.MenuSystem

import de.tr7zw.nbtapi.NBT
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import ru.piko.pikopluginlib.Utils.Extends.Bukkit.ItemStackExtend.item
import ru.piko.pikopluginlib.Utils.Extends.Bukkit.ItemStackExtend.meta

object MenuItems {
	
	val FILLER_GLASS: ItemStack =
		Material.GRAY_STAINED_GLASS_PANE.item.menuFlag("filter").meta {
			displayName(Component.empty())
			isHideTooltip = true
		}
	val FILLER_LIGHT_GLASS: ItemStack =
		Material.LIGHT_GRAY_STAINED_GLASS_PANE.item.menuFlag("filter").meta {
			displayName(Component.empty())
			isHideTooltip = true
		}
	
	val ARROW_BACK: ItemStack =
		Material.ARROW.item.menuFlag("back").meta {
			displayName(Component.text("Назад", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))
		}
	
	val BARRIER_CLOSE: ItemStack =
		Material.BARRIER.item.menuFlag("close").meta {
			displayName(Component.text("Закрыть", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false))
		}
	
	
	private const val PIKOLIB_MENU = "pikolib:menu"
	
	private fun ItemStack.menuFlag(id: String): ItemStack {
		NBT.modify(this) {
			it.setString(PIKOLIB_MENU, id)
		}
		return this
	}
	
	fun setPikoMenuFlag(item: ItemStack, flag: String): ItemStack {
		return item.menuFlag(flag)
	}
	
	fun isPikoMenuFlag(item: ItemStack, flag: String? = null): Boolean {
		return NBT.get<Boolean>(item) {
			if (it.hasTag(PIKOLIB_MENU))
				if (flag == null) true else it.getString(PIKOLIB_MENU) == flag
			else
				false
		}
	}
	
	fun getPikoMenuFlag(item: ItemStack): String? {
		return NBT.get<String>(item) {
			if (it.hasTag(PIKOLIB_MENU))
				it.getString(PIKOLIB_MENU)
			else
				null
		}
	}
	
}