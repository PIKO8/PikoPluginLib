package ru.piko.pikopluginlib.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.java.JavaPlugin;
import ru.piko.pikopluginlib.Main;
import ru.piko.pikopluginlib.PikoPlugin;
import ru.piko.pikopluginlib.PikoPluginData;

public class PluginListener implements Listener {

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
                Main.getPlugin().getLogger().warning("How did this even happen???!!!");
                Main.getPlugin().addDisablePikoPlugin(plugin.getPluginId());
                data = Main.getPlugin().getPikoPluginData(plugin.getPluginId());
            } else {
                data.disable();
            }
            String name = data.getNamePlugin();
            if (name == null) name = "???";
            Main.getPlugin().getLogger().info("The " + name + "plugin is disabled. The status in the PikoPlugins system is set to disable.");
        }
    }

}
