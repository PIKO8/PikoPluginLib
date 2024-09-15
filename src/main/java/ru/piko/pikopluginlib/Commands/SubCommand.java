package ru.piko.pikopluginlib.Commands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public abstract class SubCommand {

    private CommandManager commandManager = null;

    public void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    protected CommandManager getCommandManager() {
        return commandManager;
    }

    public abstract String getName();
    public abstract String getDescription();
    public abstract String getSyntax();
    public abstract String getPermission(@NotNull CommandSender sender, @NotNull String[] args);
    public abstract void perform(@NotNull CommandSender sender, @NotNull String[] args);
    public abstract List<String> getSubCommandArguments(@NotNull CommandSender sender, @NotNull String[] args);
    public boolean hasPermission(@NotNull CommandSender sender, @NotNull String[] args) {
        String permission = getPermission(sender, args);
        return permission == null || sender.hasPermission(permission);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubCommand that = (SubCommand) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}

