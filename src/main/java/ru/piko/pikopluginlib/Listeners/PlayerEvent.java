package ru.piko.pikopluginlib.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerEvent implements Listener {

    public Map<String, PlayerDataData> map = new HashMap<>();
    public void addData(PlayerDataData data) { map.put(data.id, data); }
    public void removeData(String id) { map.remove(id); }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {


    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {


    }

    public static class PlayerDataData {

        public String id;
        public boolean persistent;
        public boolean load;
        public boolean unload;

        public PlayerDataData(String id, boolean load, boolean unload) {
            this.id = id;
            this.load = load;
            this.unload = unload;
            this.persistent = false;
        }

        public PlayerDataData(String id, boolean persistent) {
            this.id = id;
            this.persistent = persistent;
            this.load = true;
            this.unload = true;
        }
    }
}
