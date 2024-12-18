package ru.piko.pikopluginlib.Commands.SubCommands;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ru.piko.pikopluginlib.Commands.SubCommand;
import ru.piko.pikopluginlib.Main;
import ru.piko.pikopluginlib.PikoPlugin;

import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class ReloadSubCommand extends SubCommand {
    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Восстанавливает ссылки плагинов";
    }

    @Override
    public String getSyntax() {
        return "/piko reload";
    }

    @Override
    public String getPermission(@NotNull CommandSender sender, @NotNull String[] args) {
        return "piko.admin.reload";
    }

    @Override
    public void perform(@NotNull CommandSender sender, @NotNull String[] args) {
        Main main = Main.getPlugin();
        for (Plugin plugin : getServer().getPluginManager().getPlugins()) {
            if (plugin != main && plugin instanceof JavaPlugin javaPlugin && javaPlugin instanceof PikoPlugin pikoPlugin) {
                // found a PikoPlugin instance, do something with it
                pikoPlugin.registerPikoLib();
            }
        }
    }

    @Override
    public List<String> getSubCommandArguments(@NotNull CommandSender sender, @NotNull String[] args) {
        return List.of("");
    }
}
