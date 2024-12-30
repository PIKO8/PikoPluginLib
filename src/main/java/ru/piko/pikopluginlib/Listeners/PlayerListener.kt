package ru.piko.pikopluginlib.Listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import ru.piko.pikopluginlib.PlayersData.APersistentPlayerData
import ru.piko.pikopluginlib.Utils.InternalObject.main

class PlayerListener : Listener {
    private val playerData get() = main.api.playerData
    
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val data = playerData.get(event.player.uniqueId)
        for (entry in playerData.registryMap.entries) {
            try {
                val reg = entry.value
                if (!reg.load) return
                val value = reg.function.invoke(data)
                data.addData(entry.key, value)
                if (value is APersistentPlayerData) {
                    value.loadData()
                }
            } catch (e: Throwable) {
                main.logger.warning("[ERROR] playerData key: ${entry.key} error:")
                e.printStackTrace()
            }
        }
    }
    
    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val uniqueId = event.player.uniqueId
        val data = playerData.get(uniqueId)
        for (entry in playerData.registryMap.entries) {
            try {
                val reg = entry.value
                if (!reg.unload) return
                val value = reg.function.invoke(data)
                if (value is APersistentPlayerData) {
                    value.saveData()
                }
                data.removeData(entry.key)
            } catch (e: Throwable) {
                main.logger.warning("[ERROR] playerData key: ${entry.key} error:")
                e.printStackTrace()
            }
        }
        playerData.remove(uniqueId)
    }
}