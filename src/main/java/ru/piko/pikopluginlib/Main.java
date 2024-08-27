package ru.piko.pikopluginlib;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import ru.piko.pikopluginlib.Items.ItemBuilder;
import ru.piko.pikopluginlib.Items.ItemBuilderData;
import ru.piko.pikopluginlib.Items.ItemBuilderNBT;
import ru.piko.pikopluginlib.Listeners.MenuEvent;
import ru.piko.pikopluginlib.PlayersData.APlayerData;
import ru.piko.pikopluginlib.PlayersData.PlayerData;

import java.util.*;

public final class Main extends PikoPlugin {

    private static Main plugin;

    private final Map<String, PikoPlugin> pikoPluginMap = new HashMap<>();
    /**
     * A map that stores player data for each player by their UUID.
     */
    private final Map<UUID, PlayerData> playerDataMap = new HashMap<>();


    @Override
    public String getPluginId() {
        return "lib";
    }

    @Override
    public void onStart() {

        ItemStack[] items = new ItemStack[5];

ItemStack item = new ItemBuilder(Material.GOLDEN_PICKAXE) // Создаём предмет "Золотая кирка"
    .setDisplayName("&6Прочная золотая кирка") // Устанавливаем имя предмета (&6 - золотой(gold) цвет)
    .setAmount(1) // Устанавливаем количество на 1 (бесполезно так как по умолчанию 1, но для примера)
    .setLore("&fКакое-то описание", // Устанавливаем описание (&f - белый(white) цвет)
            "&f",
            "&d Эффективность &b5", // &d - светло-пурпурный(light purple) цвет
            "&d Прочность &b10", // &b - голубой(aqua) цвет
            "&d Починка"
    )
    .addEnchantment(Enchantment.DIG_SPEED, 5) // Добавляем Эффективность 5 уровня
    .addEnchantment(Enchantment.DURABILITY, 10) // Добавляем Прочность 10 уровня
    .addEnchantment(Enchantment.MENDING, 1) // Добавляем Починку 1 уровня
    .addItemFlags(ItemFlag.HIDE_ENCHANTS) // Скрываем зачарования так как сами написали в описании
    .build(); // Собираем предмет в ItemStack

item = new ItemBuilder(item) // Создаём ItemBuilder от уже готового предмета
    .modify(new ItemBuilderNBT()) // Модификация с помощью ItemBuilderNBT
    .withNBT(nbtBuilder -> nbtBuilder
        // Создаём(или получаем) объект ":" тут поддерживается в варианте "namespace_id:name_id"(не обязательно)
        .getOrCreateObject("test:test_object")
        .setInteger("int", 100) // Устанавливаем переменную "int" на 100
        .setItemArray("inventory", items) // Устанавливаем ItemStack[] в "inventory"
        .closeObject() // Закрываем объект
        .setBoolean("test:bool", true) // Устанавливаем "test:bool" на true
    ) // Закрываем функцию и метод withNBT
    .exitModify() // заканчиваем модификацию с помощью ItemBuilderNBT
    .build(); // Собираем предмет в ItemStack


item = new ItemBuilder(item) // Создаём ItemBuilder от уже готового предмета
    .modify(new ItemBuilderData(Main.getPlugin())) // Модификация с помощью ItemBuilderData
    .setData("int", PersistentDataType.INTEGER, 10)
    .setData("plugin_id", PersistentDataType.STRING, Main.getPlugin().getPluginId())
    .exitModify() // заканчиваем модификацию с помощью ItemBuilderData
    .build(); // Собираем предмет в ItemStack

// ...

ItemBuilderData itemBuilderData = new ItemBuilder(item) // Создаём ItemBuilder от уже готового предмета
    .modify(new ItemBuilderData(Main.getPlugin())); // Модификация с помощью ItemBuilderData
    // Не заканчиваем модификацию, что бы получить экземпляр ItemBuilderData

int integer = itemBuilderData.getData("int", PersistentDataType.INTEGER);
String pluginId =   itemBuilderData.getData("plugin_id", PersistentDataType.STRING);

    }

    @Override
    public void onStop() {}

    @Override
    public void onEnable() {
        plugin = this;
        this.pluginId = getPluginId();
        addPikoPlugin(pluginId, this);
        getServer().getPluginManager().registerEvents(new MenuEvent(), this);
    }

    public static Main getPlugin() {
        return plugin;
    }

    public void addPikoPlugin(String pluginId, PikoPlugin pikoPlugin) {
        if (!pikoPluginMap.containsKey(pluginId)) {
            pikoPluginMap.put(pluginId, pikoPlugin);
        } else {
            // Логирование или обработка ситуации, когда плагин уже зарегистрирован
            System.out.println("Плагин с идентификатором " + pluginId + " уже зарегистрирован.");
        }
    }

    public boolean hasPikoPlugin(String id) {
        return pikoPluginMap.containsKey(id);
    }

    public PikoPlugin getPikoPlugin(String id) {
        return pikoPluginMap.get(id);
    }

    public void removePikoPlugin(String id) {
        pikoPluginMap.remove(id);
    }

    // PLAYER DATA
    public PlayerData getPlayerData(UUID owner) {
        if (playerDataMap.containsKey(owner)) {
            return playerDataMap.get(owner);
        }
        PlayerData data = new PlayerData(Bukkit.getPlayer(owner));
        playerDataMap.put(owner, data);
        return data;
    }

    public void removePlayerData(UUID owner) {
        playerDataMap.remove(owner);
    }

}
