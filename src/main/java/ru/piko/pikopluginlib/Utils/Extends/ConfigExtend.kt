package ru.piko.pikopluginlib.Utils.Extends

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.ConfigurationSection

object ConfigExtend {
	fun ConfigurationSection.setLiteLocation(key: String, location: Location, int: Boolean = false) {
		this.set("$key.world", location.world.name)
		if (int) {
			this.set("$key.x", location.blockX)
			this.set("$key.y", location.blockY)
			this.set("$key.z", location.blockZ)
		} else {
			this.set("$key.x", location.x)
			this.set("$key.y", location.y)
			this.set("$key.z", location.z)
		}
	}
	
	fun ConfigurationSection.getLiteLocation(key: String, int: Boolean = false): Location? {
		val worldName = this.getString("$key.world") ?: return null
		val world = Bukkit.getWorld(worldName) ?: return null
		
		return if (int) {
			Location(world,
				this.getInt("$key.x").toDouble(),
				this.getInt("$key.y").toDouble(),
				this.getInt("$key.z").toDouble()
			)
		} else {
			Location(world,
				this.getDouble("$key.x"),
				this.getDouble("$key.y"),
				this.getDouble("$key.z")
			)
		}
	}
}