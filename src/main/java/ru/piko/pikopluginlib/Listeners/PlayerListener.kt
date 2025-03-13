package ru.piko.pikopluginlib.Listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import ru.piko.pikopluginlib.PlayersData.APersistentPlayerData
import ru.piko.pikopluginlib.PlayersData.CodecPlayerDataRegistry
import ru.piko.pikopluginlib.PlayersData.CommonPlayerDataRegistry
import ru.piko.pikopluginlib.Utils.InternalObject.main

class PlayerListener : Listener {
  private val playerData get() = main.api.playerData
  
  @EventHandler
  fun onPlayerJoin(event: PlayerJoinEvent) {
    val data = playerData.get(event.player.uniqueId)
    for (entry in playerData.registryMap.entries) {
      try {
        val reg = entry.value
        if (!reg.load) continue
	      
	      val value = reg.create()
	      
	      if (value is APersistentPlayerData) {
		      value.loadData()
	      }
	      data.addData(entry.key, value)
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
        if (!reg.unload) continue
	      
	      val regData = data.getData(reg.id) ?: continue
	      if (regData is APersistentPlayerData) {
		      regData.saveData()
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