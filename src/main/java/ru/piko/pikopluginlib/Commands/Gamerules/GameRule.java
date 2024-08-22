package ru.piko.pikopluginlib.Commands.Gamerules;

import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static ru.piko.pikopluginlib.Utils.UText.color;

public class GameRule {

    private String name;
    private String helpMessage;
    private Supplier<String> getValue;
    private Consumer<String> setValue;
    private List<String> possibleValues;
    private boolean isBooleanRule;

    public GameRule(String name) {
        this.name = name;
    }

    public GameRule setHelp(String helpMessage) {
        this.helpMessage = helpMessage;
        return this;
    }

    public GameRule setGetValue(Supplier<String> getValue) {
        this.getValue = getValue;
        return this;
    }

    public GameRule setSetValue(Consumer<String> setValue) {
        this.setValue = setValue;
        return this;
    }

    public GameRule setPossibleValues(List<String> possibleValues) {
        this.possibleValues = possibleValues;
        return this;
    }

    public GameRule setBooleanRule(boolean isBooleanRule) {
        this.isBooleanRule = isBooleanRule;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getHelpMessage() {
        return helpMessage;
    }

    public String getValue() {
        return getValue != null ? getValue.get() : null;
    }

    public void setValue(String value) {
        if (setValue != null) {
            setValue.accept(value);
        }
    }

    public List<String> getPossibleValues() {
        return possibleValues;
    }

    public boolean isBooleanRule() {
        return isBooleanRule;
    }

    public void performRule(CommandSender sender, String action, String value) {
        switch (action.toLowerCase()) {
            case "get" -> sender.sendMessage(color("&f" + getName() + ": &6" + getValue()));
            case "set" -> {
                setValue(value);
                sender.sendMessage(color("&f" + getName() + ": &6" + getValue()));
            }
            case "help" -> sender.sendMessage(color(getHelpMessage()));
            default -> sender.sendMessage(color("&cНеизвестная команда: " + action));
        }
    }
}
