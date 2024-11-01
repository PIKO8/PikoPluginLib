package ru.piko.pikopluginlib.Commands.Gamerules;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class GameRule {

    private final String name;
    private String helpMessage = "Сообщение для этого правила не установлено";
    private Supplier<String> getValue = null;
    private Consumer<String> setValue = null;
    private List<String> possibleValues = null;
    private Supplier<List<String>> dynamicPossibleValues = null;
    private boolean isBooleanRule = false;
    private String permission = null;
    private boolean isCustomPermission = false;

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

    public GameRule setDynamicPossibleValues(Supplier<List<String>> dynamicPossibleValues) {
        this.dynamicPossibleValues = dynamicPossibleValues;
        return this;
    }

    public GameRule setBooleanRule(boolean isBooleanRule) {
        this.isBooleanRule = isBooleanRule;
        return this;
    }

    public GameRule setPermission(String permission, boolean isCustomPermission) {
        this.permission = permission;
        this.isCustomPermission = isCustomPermission;
        return this;
    }

    public <T extends AGameRuleModification> T modify(T modification) {
        modification.gameRule = this;
        return modification;
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
        return dynamicPossibleValues != null ? dynamicPossibleValues.get() : possibleValues;
    }

    public boolean isBooleanRule() {
        return isBooleanRule;
    }

    public String getPermission() {
        return permission;
    }

    public boolean isCustomPermission() {
        return isCustomPermission;
    }
}
