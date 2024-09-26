package ru.piko.pikopluginlib;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class PikoPluginData {

    private final String id;
    private final String name_plugin;
    private PikoPlugin plugin = null; // Сам плагин может быть null
    private @NotNull EStatusPlugin status = EStatusPlugin.BLOCKED; // Статус плагина
    private File file; // Нужен, что бы можно было включить плагин

    public PikoPluginData(String id, PikoPlugin plugin, boolean blocked) {
        this.id = id;
        this.plugin = plugin;
        this.status = blocked ? EStatusPlugin.BLOCKED : EStatusPlugin.ENABLE;
        this.file = plugin.getPluginFile();
        this.name_plugin = plugin.getName();
    }

    public PikoPluginData(String id) {
        this.id = id;
        this.plugin = null;
        this.status = EStatusPlugin.DISABLE;
        this.file = null;
        this.name_plugin = null;
    }

    public void activate(PikoPlugin plugin, boolean blocked) {
        setPlugin(plugin);
        setStatus(blocked ? EStatusPlugin.BLOCKED : EStatusPlugin.ENABLE);
    }

    public void disable() {
        setPlugin(null);
        setStatus(EStatusPlugin.DISABLE);
    }

    public String getNamePlugin() {
        return name_plugin;
    }

    public String getId() {
        return id;
    }

    public PikoPlugin getPlugin() {
        return plugin;
    }

    public void setPlugin(PikoPlugin plugin) {
        this.plugin = plugin;
    }

    public @NotNull EStatusPlugin getStatus() {
        return status;
    }

    public void setStatus(@NotNull EStatusPlugin status) {
        this.status = status;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

}
