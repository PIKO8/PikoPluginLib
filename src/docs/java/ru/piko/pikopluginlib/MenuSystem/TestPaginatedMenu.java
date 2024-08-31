package ru.piko.pikopluginlib.MenuSystem;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import ru.piko.pikopluginlib.PlayersData.PlayerData;

public class TestPaginatedMenu extends PaginatedMenu {

    protected boolean hasVerticalColumns = true; // Заполнять ли вертикальные колонки
    protected boolean hasTopRow = true; // Заполнять ли верхнюю строчку

    public TestPaginatedMenu(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public String getMenuName() {
        return "Тестовое меню со страницами";
    }

    @Override
    public int getSlots() {
        return 27; // (18)?, 27, 36, 45, 54 значения могут быть только такими.
        // 9 не поддерживается вообще
        // 18 не поддерживается если hasTopRow = false
    }

    @Override
    public void setMenuItems() {
        // вызывается при открытии меню

        // Добавляет нижнюю панель навигации
        // Добавляет так же зависимости от hasVerticalColumns и hasTopRow
        // Принимает предмет которым заполняет
        // Подсчитывает maxItemsPerPage - максимальное предметов на странице
        addMenuBorder(super.FILLER_GLASS);
    }

    @Override
    public void closeMenu(InventoryCloseEvent e) {
        // вызывается при закрытии меню
    }

    @Override
    public void clickMenu(InventoryClickEvent e) {
        // вызывается при клике по предмету меню
    }
}
