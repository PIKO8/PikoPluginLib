package ru.piko.allpikoplugin.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static ru.piko.allpikoplugin.Utils.UText.color;

public class CommandManager implements TabExecutor {

    private ArrayList<SubCommand> subCommands = new ArrayList<>();
    public ArrayList<SubCommand> getSubCommands(){
        return subCommands;
    }
    public void addSubCommand(SubCommand command) { if (!subCommands.contains(command)) { subCommands.add(command); } }
    public void clearSubCommands() {
        subCommands.clear();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (strings.length > 0){
            for (int i = 0; i < getSubCommands().size(); i++){
                if (strings[0].equalsIgnoreCase(getSubCommands().get(i).getName())){
                    if (commandSender instanceof Player p) {
                        if (!p.hasPermission(getSubCommands().get(i).getPermission())) {
                            p.sendMessage(color("Недостаточно прав!"));
                            return true;
                        }
                    }
                    getSubCommands().get(i).perform(commandSender, strings);
                }
            }
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 1){
            ArrayList<String> subcommandsArguments = new ArrayList<>();

            for (int i = 0; i < getSubCommands().size(); i++){
                if (commandSender.hasPermission(getSubCommands().get(i).getPermission())) {
                    subcommandsArguments.add(getSubCommands().get(i).getName());
                }
            }
            return subcommandsArguments;
        }else if(strings.length >= 2){
            for (int i = 0; i < getSubCommands().size(); i++){
                if (strings[0].equalsIgnoreCase(getSubCommands().get(i).getName())){
                    return getSubCommands().get(i).getSubCommandArguments(commandSender, strings);
                }
            }
        }

        return null;
    }
}
