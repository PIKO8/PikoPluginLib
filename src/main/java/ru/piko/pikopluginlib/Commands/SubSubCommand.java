package ru.piko.pikopluginlib.Commands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ru.piko.pikopluginlib.Utils.UText.color;

public abstract class SubSubCommand extends SubCommand {

    private final ArrayList<SubCommand> subCommands = new ArrayList<>();

    public void addSubCommand(SubCommand command) {
        if (!subCommands.contains(command)) {
            command.setCommandManager(getCommandManager());
            subCommands.add(command);
        }
    }

    public ArrayList<SubCommand> getSubCommands() {
        return subCommands;
    }

    @Override
    public void perform(@NotNull CommandSender sender, @NotNull String[] args) {
        if (args.length > 0) {
            for (SubCommand subCommand : subCommands) {
                if (args[0].equalsIgnoreCase(subCommand.getName())) {
                    if (!subCommand.hasPermission(sender, args)) {
                        sender.sendMessage(color("Недостаточно прав!"));
                        return;
                    }
                    String[] newArgs = new String[args.length - 1];
                    System.arraycopy(args, 1, newArgs, 0, args.length - 1);
                    subCommand.perform(sender, newArgs);
                    return;
                }
            }
        }
        // Если не найдена подходящая подкоманда, отправляем сообщение об ошибке
        sender.sendMessage(color("Неизвестная команда. Используйте /help для списка команд."));
    }

    @Override
    public List<String> getSubCommandArguments(@NotNull CommandSender sender, @NotNull String[] args) {
        if (args.length == 1) {
            ArrayList<String> subCommandNames = new ArrayList<>();
            for (SubCommand subCommand : subCommands) {
                if (subCommand.hasPermission(sender, args)) {
                    String subCommandName = subCommand.getName();
                    if (subCommandName.toLowerCase().startsWith(args[0].toLowerCase())) {
                        subCommandNames.add(subCommandName);
                    }
                }
            }
            return subCommandNames;
        } else if (args.length > 1) {
            for (SubCommand subCommand : subCommands) {
                if (args[0].equalsIgnoreCase(subCommand.getName())) {
                    String[] newArgs = new String[args.length - 1];
                    System.arraycopy(args, 1, newArgs, 0, args.length - 1);
                    return subCommand.getSubCommandArguments(sender, newArgs);
                }
            }
        }
        // Если не найдены аргументы для подкоманд, возвращаем пустой список
        return Collections.emptyList();
    }
}