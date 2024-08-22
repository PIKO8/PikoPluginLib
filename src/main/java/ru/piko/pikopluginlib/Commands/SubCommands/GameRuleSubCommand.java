package ru.piko.allpikoplugin.Commands.SubCommands;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ru.piko.allpikoplugin.Commands.SubCommand;
import ru.piko.allpikoplugin.Listeners.OnServerListMessage;
import ru.piko.allpikoplugin.Main;
import ru.piko.allpikoplugin.Maps.MapManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ru.piko.allpikoplugin.Utils.UText.color;

public class GameRuleSubCommand extends SubCommand {
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
        return "/piko gamerule <gamerule name> (<value>, get, help)";
    }

    @Override
    public String getPermission() { return "all_piko.commands.gamerule"; }

    @Override
    public void perform(CommandSender sender, String[] args) {
        FileConfiguration config = Main.getPlugin().getConfig();
        if (args.length < 3 || args.length > 4) { return; }
        if (args[2].equals("set") && args.length != 4) { return; }
        switch (args[1]) {
            case "DeathMinusLives" -> {
                switch (args[2]) {
                    case "help" -> sender.sendMessage(color("&6При смерти отнимается жизнь (Да - true / Нет - false)"));
                    case "get" -> sender.sendMessage(color("&fDeathMinusLives: &6" + config.getBoolean("GameRules.DeathMinusLives")));
                    case "set" -> {
                        switch (args[3]) {
                            case "true" -> {
                                MapManager.files.config.gameruleDeathMinusLives = true;
                                Main.getPlugin().getConfig().set("GameRules.DeathMinusLives", true);
                                sender.sendMessage(color("&fDeathMinusLives: &6true"));
                            }
                            case "false" -> {
                                MapManager.files.config.gameruleDeathMinusLives = false;
                                Main.getPlugin().getConfig().set("GameRules.DeathMinusLives", false);
                                sender.sendMessage(color("&fDeathMinusLives: &6false"));
                            }
                        }
                    }
                }
            }
            case "JoinLives" -> {
                switch (args[2]) {
                    case "help" -> sender.sendMessage(color("&6Для входа на сервер нужны жизни (Да - true / Нет - false)"));
                    case "get" -> sender.sendMessage(color("&fJoinLives: &6" + config.getBoolean("GameRules.JoinLives")));
                    case "set" -> {
                        switch (args[3]) {
                            case "true" -> {
                                MapManager.events.joinEvent.setLives(true);
                                Main.getPlugin().getConfig().set("GameRules.JoinLives", true);
                                sender.sendMessage(color("&fJoinLives: &6true"));
                            }
                            case "false" -> {
                                MapManager.events.joinEvent.setLives(false);
                                Main.getPlugin().getConfig().set("GameRules.JoinLives", false);
                                sender.sendMessage(color("&fJoinLives: &6false"));
                            }
                        }
                    }
                }
            }
            case "NameServerIfZeroLives" -> {
                switch (args[2]) {
                    case "help" -> sender.sendMessage(color("&6Имя сервера на который будет отправлен игрок если у него закончаться жизни"));
                    case "get" -> sender.sendMessage(color("&fNameServerIfZeroLives: &6" + config.getString("GameRules.NameServerIfZeroLives")));
                    case "set" -> {
                        Main.getPlugin().getConfig().set("GameRules.NameServerIfZeroLives", args[3]);
                        sender.sendMessage(color("&fNameServerIfZeroLives: &6" + args[3]));
                    }
                }
            }
        }
        Main.getPlugin().saveConfig();
    }

    @Override
    public List<String> getSubCommandArguments(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 2) {
            list.add("DeathMinusLives");
            list.add("JoinLives");
            list.add("NameServerIfZeroLives");
        }
        if (args.length >= 3) {
            if (args[1].equals("DeathMinusLives") || args[1].equals("JoinLives") || args[1].equals("NameServerIfZeroLives")) {
                if (args.length == 3) {
                    list.add("help");
                    list.add("get");
                    list.add("set");
                }
                if (args.length == 4 && args[2].equals("set")) {
                    if (!args[1].equals("NameServerIfZeroLives")) {
                        list.add("true");
                        list.add("false");
                    } else {
                        if (MapManager.files.config.settingBungeeCord) {
                            if (sender instanceof Player player) {
                                OnServerListMessage mess = MapManager.events.serverListMessage;
                                mess.Message(player);
                                String[] servers = mess.getList();
                                if (servers != null) {
                                    Collections.addAll(list, servers);
                                }
                            } else {
                                list.add("Для получения списка серверов требуется игрок");
                            }
                        } else {
                            list.add("BungeeCord выключен в конфиге");
                        }
                    }
                }
            } else {
                list.add("Ошибка_в_предыдущих_значениях");
            }
        }
        return list;
    }
}
