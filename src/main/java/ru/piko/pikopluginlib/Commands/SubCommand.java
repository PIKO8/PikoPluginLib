package ru.piko.allpikoplugin.Commands;


import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class SubCommand {

    public abstract String getName();

    public abstract String getDescription();

    public abstract String getSyntax();
    public abstract String getPermission();

    public abstract void perform(CommandSender sender, String args[]);

    public abstract List<String> getSubCommandArguments(CommandSender sender, String args[]);

}

