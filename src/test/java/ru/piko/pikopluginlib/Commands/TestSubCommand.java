package ru.piko.pikopluginlib.Commands;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class TestSubCommand extends SubCommand{

    @Override
    public String getName() {
        return "name_command"; // название команды например "/test name_command" нельзя использовать пробелы
    }

    @Override
    public String getDescription() {
        return ""; // описание команды пока не используется
    }

    @Override
    public String getSyntax() {
        return "/test <name_command> <params>"; //
    }

    @Override
    public String getPermission(CommandSender sender, String[] args) {
        return ""; // права на выполнение команды строка "null" выдаст false
        // так же есть аргументы sender и args что бы точно узнать команду
        // ! это так же вызывается для списка аргументов по этому нужно проверять args[i] != null
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        // Выполнение команды
        // args[0] равен this.getName()
    }

    @Override
    public List<String> getSubCommandArguments(CommandSender sender, String[] args) {
        // Возращение списка возможных команд
        // Это вызывается при args.length >= 2 так как args[0] это эта подкоманда, то есть args[0] равен this.getName()
        List<String> result = new ArrayList<>();

        // При длине == 2 последний это args[1]
        if (args.length == 2) {
            // Тут список для "1" /test 0 1
            result.add("1");
        }
        else if (args.length == 3) {
            // Тут список для "2" /test 0 1 2
            result.add("2");
        }
        else if (args.length == 4) {
            // Тут список для "3" /test 0 1 2 3
            result.add("3");
        }
        // И так далее
        else { // Если ни одно не случилось
            // При пустом списке будет список игроков сервера
            result = List.of();

            // Можно так (и да на русском тоже можно), пробелы не желательны(но возможны)
            result = List.of("Аргументы_закончились");
        }

        return result;
    }
}
