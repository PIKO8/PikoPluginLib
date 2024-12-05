package ru.piko.pikopluginlib.Utils.Extends

import org.bukkit.Location
import org.bukkit.entity.Item
import org.bukkit.inventory.ItemStack

@JvmName("dropItemNaturallyNullable")
fun Location.dropItemNaturally(item: ItemStack?): Item? {
	item ?: return null
	return this.world.dropItemNaturally(this, item)
}

@JvmName("dropItemNaturallyNonNull")
fun Location.dropItemNaturally(item: ItemStack): Item {
	return this.world.dropItemNaturally(this, item)
}

@JvmName("dropItemNullable")
fun Location.dropItem(item: ItemStack?): Item? {
	item ?: return null
	return this.world.dropItem(this, item)
}

@JvmName("dropItemNonNull")
fun Location.dropItem(item: ItemStack): Item {
	return this.world.dropItem(this, item)
}