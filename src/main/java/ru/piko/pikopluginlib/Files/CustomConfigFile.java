package ru.piko.pikopluginlib.Files;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.piko.pikopluginlib.Main;

import java.io.File;

public abstract class CustomConfigFile {

    protected File file;
    protected FileConfiguration fileConfiguration;

    public CustomConfigFile(String relativePath) {
        this(Main.Companion.getPlugin().getDataFolder(), relativePath);
    }
    public CustomConfigFile(File dataFolder, String relativePath) {
        file = new File(dataFolder, relativePath + ".yml");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
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

    public void reload() {
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }
}
