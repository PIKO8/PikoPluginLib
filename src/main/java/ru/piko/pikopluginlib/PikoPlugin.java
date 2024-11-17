package ru.piko.pikopluginlib;

import org.bukkit.NamespacedKey;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ru.piko.pikopluginlib.Commands.CommandManager;
import ru.piko.pikopluginlib.Commands.Gamerules.GameRuleStandardSave;
import ru.piko.pikopluginlib.Functions.FunctionAbstract;
import ru.piko.pikopluginlib.PlayersData.PlayerData;
import ru.piko.pikopluginlib.PlayersData.PlayerDataRegistry;

import java.io.File;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public abstract class PikoPlugin extends JavaPlugin {

    // <editor-fold defaultstate="collapsed" desc="Variables">
    /**
     * Manages the standard save state of game rules.
     */
    private GameRuleStandardSave gameRuleStandardSave;

    private final HashMap<String, CommandManager> commandManagerMap = new HashMap<>();

    /**
     * Unique identifier for the plugin.
     * Format - "namespace.author.plugin_name"; Example "ru.piko.lib"
     */
    protected String pluginId;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Abstracts">
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
    public abstract void onRegister();
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="onEnable & onDisable">
    /**
     * Called by Bukkit when the plugin is enabled. Initializes the plugin ID and calls {@link #onStart()}.
     */
    @Override
    public void onEnable() {
        registerPikoLib();
        try {
            onStart();
        } catch (Exception e) {
            Main.getPlugin().getLogger().warning("[ERROR] Plugin - " + getPluginId() + " in onRegister error message: " + e.getMessage() + " stack track:");
            e.printStackTrace();
        }
    }

    public void registerPikoLib() {
        this.pluginId = getPluginId();
        Main plugin = Main.getPlugin();
        if (plugin != null) {
            plugin.addPikoPlugin(pluginId, this);
        } else {
            Main.getPlugin().getLogger().info("I couldn 't add it " + getPluginId() + " to the PikoPlugins system");
        }
        try {
            onRegister();
        } catch (Exception e) {
            Main.getPlugin().getLogger().warning("[ERROR] Plugin - " + getPluginId() + " in onRegister error message: " + e.getMessage() + " stack track:");
            e.printStackTrace();
        }
    }
    /**
     * Called by Bukkit when the plugin is disabled. Calls {@link #onStop()} and removes the plugin from the main registry.
     */
    @Override
    public void onDisable() {
        try {
            onStop();
        } catch (Exception e) {
            Main.getPlugin().getLogger().warning("[ERROR] Plugin - " + getPluginId() + " in onStop error message: " + e.getMessage() + " stack track:");
            e.printStackTrace();
        }
        FunctionAbstract.Static.destroyAll(this);
        Main.getPlugin().disablePikoPlugin(pluginId);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Command Manager">
    /**
     * Creates a CommandManager for handling commands related to this plugin.
     *
     * @param mainCommand The main command associated with the CommandManager.
     * @return A new instance of CommandManager.
     * @throws IllegalArgumentException if the command is not registered.
     */
    public CommandManager createCommandManager(@NotNull String mainCommand) {
        CommandManager commandManager = new CommandManager(pluginId, mainCommand);
        PluginCommand command = this.getCommand(mainCommand);
        if (command != null) {
            command.setExecutor(commandManager);
            commandManagerMap.put(mainCommand, commandManager);
        } else {
            throw new IllegalArgumentException("Command '" + mainCommand + "' is not registered.");
        }
        return commandManager;
    }

    public CommandManager getOrCreateCommandManager(@NotNull String mainCommand) {
        if (hasCommandManager(mainCommand)) {
            return getCommandManager(mainCommand);
        }
        return createCommandManager(mainCommand);
    }

    public CommandManager getCommandManager(@NotNull String mainCommand) {
        if (commandManagerMap.containsKey(mainCommand)) {
            return commandManagerMap.get(mainCommand);
        }
        return null;
    }

    public boolean hasCommandManager(@NotNull String mainCommand) {
        return commandManagerMap.containsKey(mainCommand);
    }

    public void addCommandManager(@NotNull String mainCommand, @NotNull CommandManager manager) {
        if (!commandManagerMap.containsKey(mainCommand)) {
            commandManagerMap.put(mainCommand, manager);
        }
        else {
            commandManagerMap.replace(mainCommand, manager);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Piko Plugin">
    /**
     * Attempts to retrieve a PikoPlugin by its ID.
     *
     * @param id The ID of the plugin to retrieve.
     * @return The PikoPlugin instance, or null if not found.
     * @deprecated see {@link #tryGetPikoPluginData(String)}
     */
    @Deprecated
    public Optional<PikoPlugin> tryGetPikoPlugin(@NotNull String id) {
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
     * @deprecated see {@link #getPikoPluginData(String)}
     */
    @Deprecated
    public PikoPlugin getPikoPlugin(@NotNull String id) {
        return Main.getPlugin().getPikoPlugin(id);
    }

    /**
     * Checks if a PikoPlugin with the specified ID exists.
     *
     * @param id The ID of the plugin to check.
     * @return True if the plugin exists, false otherwise.
     */
    public boolean hasPikoPlugin(@NotNull String id) {
        return Main.getPlugin().hasPikoPlugin(id);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Piko Plugin Data">

    /**
     * Retrieves a PikoPlugin by its ID.
     *
     * @param id The ID of the plugin to retrieve.
     * @return The PikoPluginData instance.
     */
    public PikoPluginData getPikoPluginData(@NotNull String id) {
        return Main.getPlugin().getPikoPluginData(id);
    }

    /**
     * Attempts to retrieve a PikoPlugin by its ID.
     *
     * @param id The ID of the plugin to retrieve.
     * @return The PikoPlugin instance, or null if not found.
     */
    public Optional<PikoPluginData> tryGetPikoPluginData(@NotNull String id) {
        if (Main.getPlugin().hasPikoPlugin(id)) {
            return Optional.of(Main.getPlugin().getPikoPluginData(id));
        }
        return Optional.empty();
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Other">

    public EStatusPlugin getStatus() {
        return Main.getPlugin().getPikoPluginData(pluginId).getStatus();
    }

    public File getPluginFile() {
        return getFile();
    }

    /**
     * Creates a NamespacedKey for this plugin using the provided ID.
     *
     * @param id The ID to use in the NamespacedKey.
     * @return A new NamespacedKey associated with this plugin.
     */
    public NamespacedKey getNamespacedKey(@NotNull String id) {
        return new NamespacedKey(this, id);
    }

    /**
     * Retrieves the GameRuleStandardSave instance, creating it if it doesn't exist.
     *
     * @return The GameRuleStandardSave instance associated with this plugin.
     */
    public @NotNull GameRuleStandardSave getOrCreateGameRuleStandardSave() {
        if (gameRuleStandardSave == null) {
            gameRuleStandardSave = new GameRuleStandardSave(pluginId);
        }
        return gameRuleStandardSave;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Player Data">
    public boolean hasOnlinePlayerData(UUID owner) {
        return Main.getPlugin().hasOnlinePlayerData(owner);
    }

    public PlayerData getPlayerData(UUID owner) {
        return Main.getPlugin().getPlayerData(owner);
    }

    public void removePlayerData(UUID owner) {
        Main.getPlugin().removePlayerData(owner);
    }

      // <editor-fold-sub defaultstate="collapsed" desc="">
    public void registerPlayerData(String id, PlayerDataRegistry registry) {
        Main.getPlugin().registerPlayerData(id, registry);
    }

    public void unregisterPlayerData(String id) {
        Main.getPlugin().unregisterPlayerData(id);
    }

    public HashMap<String, PlayerDataRegistry> getPlayerDataRegistry() {
        return Main.getPlugin().getPlayerDataRegistry();
    }
      // </editor-fold-sub>

    // </editor-fold>
}
