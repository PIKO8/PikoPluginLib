package ru.piko.pikopluginlib.Commands.Gamerules;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.piko.pikopluginlib.Commands.SubCommand;

import java.util.ArrayList;
import java.util.List;

import static ru.piko.pikopluginlib.Utils.UText.color;

public class GameRuleSubCommand extends SubCommand {

    private final List<GameRule> gameRules = new ArrayList<>();

    public void addGameRule(GameRule gameRule) {
        if (!gameRules.contains(gameRule)) {
            gameRules.add(gameRule);
        }
    }

    @Override
    public String getName() {
        return "gamerule";
    }

    @Override
    public String getDescription() {
        return "Позволяет менять правила плагина";
    }

    @Override
    public String getSyntax() {
        return "/" + getCommandManager().getCommandName() + "gamerule <gamerule name> (set, get, help) [value]";
    }

    public String getPermissionForString(String string) {
        return string + ".commands.gamerule";
    }

    @Override
    public String getPermission(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {

        return null;
    }

    @Override
    public void perform(@NotNull CommandSender sender, String @NotNull [] args) {
        if (args.length < 3 || args.length > 4) {
            sender.sendMessage(color("&cНеправильное количество аргументов. Использование: " + getSyntax()));
            return;
        }

        String ruleName = args[1];
        GameRule gameRule = getGameRuleByName(ruleName);

        if (gameRule == null) {
            sender.sendMessage(color("&cИгровое правило " + ruleName + " не найдено."));
            return;
        }

        String action = args[2];
        String value = args.length == 4 ? args[3] : null;

        // Проверка на права доступа
        if (!gameRule.isCustomPermission() && !(sender.hasPermission(getPermissionForString("piko"))
                || sender.hasPermission(getPermissionForString(getCommandManager().getCommandName()))) ||
                gameRule.isCustomPermission() && !sender.hasPermission(gameRule.getPermission())) {
            sender.sendMessage(color("&cУ вас нет прав на выполнение этого действия."));
            return;
        }

        switch (action.toLowerCase()) {
            case "get" -> sender.sendMessage(color("&f" + getName() + ": &6" + gameRule.getValue()));
            case "set" -> {
                gameRule.setValue(value);
                sender.sendMessage(color("&f" + getName() + ": &6" + gameRule.getValue()));
            }
            case "help" -> sender.sendMessage(color(gameRule.getHelpMessage()));
            default -> sender.sendMessage(color("&cНеизвестная команда: " + action));
        }
    }

    @Override
    public List<String> getSubCommandArguments(@NotNull CommandSender sender, String @NotNull [] args) {
        List<String> arguments = new ArrayList<>();

        if (args.length == 2) {
            for (GameRule gameRule : gameRules) {
                arguments.add(gameRule.getName());
            }
        } else if (args.length == 3) {
            arguments.add("help");
            arguments.add("get");
            arguments.add("set");
        } else if (args.length == 4 && "set".equalsIgnoreCase(args[2])) {
            GameRule gameRule = getGameRuleByName(args[1]);
            if (gameRule != null) {
                if (gameRule.isBooleanRule()) {
                    arguments.add("true");
                    arguments.add("false");
                } else {
                    arguments.addAll(gameRule.getPossibleValues());
                }
            }
        }

        return arguments;
    }

    private GameRule getGameRuleByName(String name) {
        for (GameRule gameRule : gameRules) {
            if (gameRule.getName().equalsIgnoreCase(name)) {
                return gameRule;
            }
        }
        return null;
    }
}
