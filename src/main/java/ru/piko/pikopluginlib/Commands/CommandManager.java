package ru.piko.pikopluginlib.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

import static ru.piko.pikopluginlib.Utils.UText.color;

@SuppressWarnings("NullableProblems")
public class CommandManager implements TabExecutor {

    private final String namePikoPlugin;
    private final String commandName;
    private final ArrayList<SubCommand> subCommands = new ArrayList<>();
    public ArrayList<SubCommand> getSubCommands(){
        return subCommands;
    }
    public void addSubCommand(SubCommand command) {
        if (!subCommands.contains(command))
        {
            command.setCommandManager(this);
            subCommands.add(command);
        }
    }
    public void clearSubCommands() {
        subCommands.clear();
    }

    public CommandManager(String namePikoPlugin, String commandName) {
        this.namePikoPlugin = namePikoPlugin;
        this.commandName = commandName;
    }

    public String getNamePikoPlugin() {
        return namePikoPlugin;
    }

    public String getCommandName() {
        return commandName;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (strings.length > 0){
            for (int i = 0; i < getSubCommands().size(); i++){
                if (strings[0].equalsIgnoreCase(getSubCommands().get(i).getName())){
                    if (!getSubCommands().get(i).hasPermission(commandSender, strings)) {
                        commandSender.sendMessage(color("Недостаточно прав!"));
                        return true;
                    }
                    getSubCommands().get(i).perform(commandSender, strings);
                }
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 1){
            ArrayList<String> subcommandsArguments = new ArrayList<>();

            for (int i = 0; i < getSubCommands().size(); i++){
                if (getSubCommands().get(i).hasPermission(commandSender, strings)) {
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
