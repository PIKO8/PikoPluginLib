package ru.piko.pikopluginlib.Commands;

import org.bukkit.command.CommandSender;
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
    public abstract String getPermission(CommandSender sender, String[] args);
    public abstract void perform(CommandSender sender, String args[]);
    public abstract List<String> getSubCommandArguments(CommandSender sender, String args[]);
    public boolean hasPermission(CommandSender sender, String[] args) {
        String permission = getPermission(sender, args);
        return !(permission == null || sender.hasPermission(permission));
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

