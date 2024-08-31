package ru.piko.pikopluginlib.MenuSystem;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import ru.piko.pikopluginlib.PlayerData.TestPlayerDataMenu;
import ru.piko.pikopluginlib.PlayersData.PlayerData;

public class TestMenu extends Menu {

    private TestPlayerDataMenu data; // Создаём переменную для кастомной даты

    public TestMenu(PlayerData playerData) {
        super(playerData);
        // Сохраняем кастомную дату в переменную
        // Предполагается что она была создана до открытия меню
        data = (TestPlayerDataMenu) playerData.getData("plugin.menu.test");
    }

    @Override
    public String getMenuName() {
        return "Тестовое меню";
    }

    @Override
    public int getSlots() {
        return 27; // 9, 18, 27, 36, 45, 54 значения могут быть только такими
    }

    @Override
    public void setMenuItems() {
        setFillerGlass(); // Заполняем весь инвентарь серым стеклом
        inventory.setItem(13, new ItemStack(Material.AIR));
        // вызывается при открытии меню
    }

    @Override
    public void closeMenu(InventoryCloseEvent e) {
        // вызывается при закрытии меню
        data.setItem(inventory.getItem(13)); // Получаем и сохраняем предмет из 13 слота
    }

    @Override
    public void clickMenu(InventoryClickEvent e) {
        // вызывается при клике по предмету меню
        if (e.getSlot() == 13) { // Если клик по 13 слоту
            e.setCancelled(false); // То отменяем отмену ивента
            // до вызова данного метода e.setCancelled устанавливается на true поэтому "отменяем отмену"
        }
    }
}
