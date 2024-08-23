package ru.piko.pikopluginlib;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import ru.piko.pikopluginlib.Commands.CommandManager;
import ru.piko.pikopluginlib.Commands.Gamerules.GameRuleStandardSave;

import java.util.Optional;

public abstract class PikoPlugin extends JavaPlugin {


    /**
     * Manages the standard save state of game rules.
     */
    private GameRuleStandardSave gameRuleStandardSave;

    /**
     * Unique identifier for the plugin.
     */
    public String pluginId;

    /**
     * Returns the unique identifier for this plugin.
     *
     * @return The plugin's unique ID.
     */
    public abstract String getPluginId();

    /**
     * Called when the plugin is starting up. Should be overridden to define startup behavior.
     */
    public abstract void onStart();

    /**
     * Called when the plugin is shutting down. Should be overridden to define shutdown behavior.
     */
    public abstract void onStop();

    /**
     * Called by Bukkit when the plugin is enabled. Initializes the plugin ID and calls {@link #onStart()}.
     */
    @Override
    public void onEnable() {
        this.pluginId = getPluginId();
        Main.getPlugin().addPikoPlugin(pluginId, this);
        onStart();
    }

    /**
     * Called by Bukkit when the plugin is disabled. Calls {@link #onStop()} and removes the plugin from the main registry.
     */
    @Override
    public void onDisable() {
        onStop();
        Main.getPlugin().removePikoPlugin(pluginId);
    }


    /**
     * Creates a CommandManager for handling commands related to this plugin.
     *
     * @param mainCommand The main command associated with the CommandManager.
     * @return A new instance of CommandManager.
     */
    public CommandManager createCommandManager(String mainCommand) {
        return new CommandManager(pluginId, mainCommand);
    }

    /**
     * Attempts to retrieve a PikoPlugin by its ID.
     *
     * @param id The ID of the plugin to retrieve.
     * @return The PikoPlugin instance, or null if not found.
     */
    public Optional<PikoPlugin> tryGetPikoPlugin(String id) {
        if (Main.getPlugin().hasPikoPlugin(id)) {
            return Optional.of(Main.getPlugin().getPikoPlugin(id));
        }
        return Optional.empty();
    }

    /**
     * Retrieves a PikoPlugin by its ID.
     *
     * @param id The ID of the plugin to retrieve.
     * @return The PikoPlugin instance.
     */
    public PikoPlugin getPikoPlugin(String id) {
        return Main.getPlugin().getPikoPlugin(id);
    }

    /**
     * Checks if a PikoPlugin with the specified ID exists.
     *
     * @param id The ID of the plugin to check.
     * @return True if the plugin exists, false otherwise.
     */
    public boolean hasPikoPlugin(String id) {
        return Main.getPlugin().hasPikoPlugin(id);
    }

    /**
     * Creates a NamespacedKey for this plugin using the provided ID.
     *
     * @param id The ID to use in the NamespacedKey.
     * @return A new NamespacedKey associated with this plugin.
     */
    public NamespacedKey getNamespacedKey(String id) {
        return new NamespacedKey(this, id);
    }

    /**
     * Retrieves the GameRuleStandardSave instance, creating it if it doesn't exist.
     *
     * @return The GameRuleStandardSave instance associated with this plugin.
     */
    public GameRuleStandardSave getOrCreateGameRuleStandardSave() {
        if (gameRuleStandardSave == null) {
            gameRuleStandardSave = new GameRuleStandardSave(pluginId);
        }
        return gameRuleStandardSave;
    }
}
