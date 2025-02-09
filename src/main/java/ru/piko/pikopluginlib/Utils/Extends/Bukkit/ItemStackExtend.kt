package ru.piko.pikopluginlib.Utils.Extends.Bukkit

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

object ItemStackExtend {
	
	val Material.item: ItemStack get() = ItemStack(this)
	
	public inline fun item(
		material: Material,
		amount: Int = 1,
		stack: ItemStack.() -> Unit = {},
	): ItemStack = ItemStack(material, amount).apply(stack)
	
	public inline fun metaItem(
		material: Material,
		amount: Int = 1,
		meta: ItemMeta.() -> Unit = {},
	): ItemStack = ItemStack(material, amount).metaAs(meta)
	
	public inline fun <reified T : ItemMeta> metaItemAs(
		material: Material,
		amount: Int = 1,
		meta: T.() -> Unit,
	): ItemStack = ItemStack(material, amount).metaAs(meta)
	
	public inline fun <reified T : ItemMeta> ItemStack.metaAs(
		block: T.() -> Unit,
	): ItemStack = apply {
		itemMeta = (itemMeta as? T)?.apply(block) ?: itemMeta
	}
	
	public inline fun ItemStack.meta(
		block: ItemMeta.() -> Unit,
	): ItemStack = apply {
		itemMeta = itemMeta?.apply(block) ?: itemMeta
	}
	
	public fun ItemStack.displayName(displayName: Component?): ItemStack = metaAs<ItemMeta> {
		this.displayName(displayName)
	}
	
	public fun ItemStack.lore(lore: List<String>): ItemStack = metaAs<ItemMeta> {
		this.lore = lore
	}
	
	public inline fun Material.asItemStack(
		amount: Int = 1,
		stack: ItemStack.() -> Unit = {},
	): ItemStack = item(this, amount, stack)
	
	public inline fun Material.asMetaItemStack(
		amount: Int = 1,
		meta: ItemMeta.() -> Unit = {},
	): ItemStack = metaItem(this, amount, meta)
	
}