package ru.piko.pikopluginlib.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import ru.piko.pikopluginlib.Main
import ru.piko.pikopluginlib.PlayersData.APersistentPlayerData
import ru.piko.pikopluginlib.PlayersData.APlayerData
import ru.piko.pikopluginlib.PlayersData.PlayerDataRegistry

class PlayerListener : Listener {
    
    val plugin: Main = Main.getPlugin()
    
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val data = plugin.getPlayerData(event.player.uniqueId)
        for (entry in plugin.playerDataRegistry.entries) {
            try {
                val reg = entry.value
                if (!reg.load) return
                val value = reg.function.invoke(data)
                data.addData(entry.key, value)
                if (value is APersistentPlayerData) {
                    value.loadData()
                }
            } catch (e: Throwable) {
                Main.getPlugin().logger.warning("[ERROR] playerData key: ${entry.key} error:")
                e.printStackTrace()
            }
        }
    }
    
    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val uniqueId = event.player.uniqueId
        val data = plugin.getPlayerData(uniqueId)
        for (entry in plugin.playerDataRegistry.entries) {
            try {
                val reg = entry.value
                if (!reg.unload) return
                val value = reg.function.invoke(data)
                if (value is APersistentPlayerData) {
                    value.saveData()
                }
                data.removeData(entry.key)
            } catch (e: Throwable) {
                Main.getPlugin().logger.warning("[ERROR] playerData key: ${entry.key} error:")
                e.printStackTrace()
            }
        }
        plugin.removePlayerData(uniqueId)
    }
}