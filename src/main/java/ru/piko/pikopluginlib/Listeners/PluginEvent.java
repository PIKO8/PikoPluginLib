package ru.piko.pikopluginlib.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.java.JavaPlugin;
import ru.piko.pikopluginlib.EStatusPlugin;
import ru.piko.pikopluginlib.Main;
import ru.piko.pikopluginlib.PikoPlugin;
import ru.piko.pikopluginlib.PikoPluginData;

public class PluginEvent implements Listener {

    @EventHandler
    public void onEnablePlugin(PluginEnableEvent e) {
        if (e.getPlugin() instanceof JavaPlugin javaPlugin && javaPlugin instanceof PikoPlugin plugin) {
            if (!Main.getPlugin().hasPikoPlugin(plugin.getPluginId())) {
                PikoPluginData data = Main.getPlugin().getPikoPluginData(plugin.getPluginId());
                if (data != null) { data.activate(plugin, false); }
            } else {
                Main.getPlugin().addPikoPlugin(plugin.getPluginId(), plugin);
            }
        }
    }

    @EventHandler
    public void onDisablePlugin(PluginDisableEvent e) {
        if (e.getPlugin() instanceof JavaPlugin javaPlugin && javaPlugin instanceof PikoPlugin plugin) {
            PikoPluginData data = Main.getPlugin().getPikoPluginData(plugin.getPluginId());
            if (data == null) {
                System.out.println("How did this even happen?????!!!");
                Main.getPlugin().addDisablePikoPlugin(plugin.getPluginId());
            } else {
                data.disable();
            }
        }
    }

}
