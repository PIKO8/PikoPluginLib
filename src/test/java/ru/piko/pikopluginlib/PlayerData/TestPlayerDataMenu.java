package ru.piko.pikopluginlib.PlayerData;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ru.piko.pikopluginlib.PlayersData.ADynamicPlayerData;
import ru.piko.pikopluginlib.PlayersData.PlayerData;
import java.lang.reflect.Field;

public class TestPlayerDataMenu extends ADynamicPlayerData {

    private ItemStack item = new ItemStack(Material.AIR); // например между меню сохранить предмет

    public TestPlayerDataMenu(PlayerData data) {
        super(data);
        data.addData(getId(), this); // можно тут сразу добавить
    }

    @Override
    public String getId() {
        return "plugin.menu.test";
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }
}
