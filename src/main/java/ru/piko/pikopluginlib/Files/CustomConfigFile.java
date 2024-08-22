package ru.piko.allpikoplugin.Files;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.piko.allpikoplugin.Main;

import java.io.File;

public abstract class CustomConfigFile {

    protected File file;
    protected FileConfiguration fileConfiguration;

    public CustomConfigFile(String nameFile) {
        Main plugin = Main.getPlugin();
        file = new File(plugin.getDataFolder(), nameFile + ".yml");
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }
    public void save() {
        try {
            fileConfiguration.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
