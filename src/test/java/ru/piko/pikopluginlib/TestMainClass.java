package ru.piko.pikopluginlib;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ru.piko.pikopluginlib.Commands.CommandManager;
import ru.piko.pikopluginlib.Commands.TestSubCommand;
import ru.piko.pikopluginlib.MenuSystem.TestMenu;
import ru.piko.pikopluginlib.PlayerData.TestPlayerDataMenu;
import ru.piko.pikopluginlib.PlayersData.APlayerData;
import ru.piko.pikopluginlib.PlayersData.PlayerData;

import java.util.Iterator;
import java.util.UUID;

public class TestMainClass extends PikoPlugin {

    @Override
    public String getPluginId() {
        return "";
    }

    @Override
    public void onStart() {
        UUID player_uuid = new UUID(16, 16); // Это пример! нужно где-то получить Player и его UUID
        PlayerData playerData = this.getPlayerData(player_uuid); // нужно где-то получить player_uuid
        new TestPlayerDataMenu(playerData); // playerData.addData уже есть в конструкторе TestPlayerDataMenu по этому просто создаём
        new TestMenu(playerData).open(); // Создаём и открываем меню

        // Пример `getVariable` и `setVariable` из APlayerData
        PlayerData data = this.getPlayerData(player_uuid);
        APlayerData aPlayerData = data.getData("plugin.menu.test"); // Водим например id TestPlayerDataMenu

        // "item" - название переменной которую надо получить
        // ItemStack.class класс переменной
        // может быть null и ошибка в консоли если что-то не так введено
        ItemStack item = aPlayerData.getVariable("item", ItemStack.class);
        aPlayerData.setVariable("item", new ItemStack(Material.AIR)); // Например, устанавливаем AIR


        // this это должен быть Main класс наследуемый от PikoPlugin
        CommandManager commandManager = this.createCommandManager("test"); // не забыть в plugin.yml записать команду
        commandManager.addSubCommand(new TestSubCommand()); // Добавляем тестовую подкоманду




    }

    @Override
    public void onStop() {

    }


}
