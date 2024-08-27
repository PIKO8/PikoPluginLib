package ru.piko.pikopluginlib.MenuSystem;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ru.piko.pikopluginlib.Items.ItemBuilder;
import ru.piko.pikopluginlib.PlayersData.PlayerData;

public abstract class PaginatedMenu extends Menu {

    protected int page = 0;
    protected int maxItemsPerPage;
    protected int index = 0;

    // New parameters for configuring the menu layout
    protected boolean hasVerticalColumns = true;
    protected boolean hasTopRow = true;

    public PaginatedMenu(PlayerData playerData) {
        super(playerData);
        calculateMaxItemsPerPage();
    }

    /**
     * Configures whether the menu should display vertical columns.
     *
     * @param hasVerticalColumns true to display vertical columns, false otherwise.
     */
    public void setVerticalColumns(boolean hasVerticalColumns) {
        this.hasVerticalColumns = hasVerticalColumns;
        calculateMaxItemsPerPage();
    }

    /**
     * Configures whether the menu should display the top row.
     *
     * @param hasTopRow true to display the top row, false otherwise.
     */
    public void setTopRow(boolean hasTopRow) {
        this.hasTopRow = hasTopRow;
        calculateMaxItemsPerPage();
    }

    /**
     * Adds borders and navigation items to the menu.
     *
     * @param fillerItem The item to be used as filler, or null to use the default glass pane.
     */
    public void addMenuBorder(ItemStack fillerItem) {
        int slots = getSlots();
        ItemStack filler = fillerItem != null ? fillerItem : super.FILLER_GLASS;

        // Navigation buttons
        inventory.setItem(slots - 6, new ItemBuilder(Material.DARK_OAK_BUTTON).setDisplayName("&2<--").setCustomModelData(198).build());
        inventory.setItem(slots - 1, BARRIER_CLOSE);
        inventory.setItem(slots - 4, new ItemBuilder(Material.DARK_OAK_BUTTON).setDisplayName("&2-->").setCustomModelData(199).build());

        // Top row filler
        if (hasTopRow) {
            for (int i = 0; i < 9 && i < slots; i++) {
                if (inventory.getItem(i) == null) {
                    inventory.setItem(i, filler);
                }
            }
        }

        // Vertical columns filler
        if (hasVerticalColumns) {
            for (int i = 9; i < slots; i += 9) {
                if (i < slots && inventory.getItem(i) == null) {
                    inventory.setItem(i, filler);
                }
                if (i + 8 < slots && inventory.getItem(i + 8) == null) {
                    inventory.setItem(i + 8, filler);
                }
            }
        }

        // Bottom row filler
        for (int i = slots - 9; i < slots; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, filler);
            }
        }
    }

    /**
     * Calculates the maximum number of items that can be displayed per page based on
     * the current inventory size and layout configuration.
     */
    private void calculateMaxItemsPerPage() {
        int slots = getSlots();
        int rows = slots / 9;
        int rowsForItems = rows - 1;  // Вычитаем одну строку для навигации

        // Учитываем верхнюю строку, если она включена
        if (hasTopRow) {
            rowsForItems -= 1;
        }

        // Проверяем, есть ли строки для предметов
        if (rowsForItems <= 0) {
            maxItemsPerPage = 0;
        } else {
            if (hasVerticalColumns) {
                maxItemsPerPage = rowsForItems * 7;
            } else {
                maxItemsPerPage = rowsForItems * 9;
            }
        }
    }

    /**
     * Gets the maximum number of items that can be displayed per page.
     *
     * @return The maximum number of items per page.
     */
    public int getMaxItemsPerPage() {
        return maxItemsPerPage;
    }

    /**
     * Gets the number of slots in the menu. This method must return one of the following
     * values: 27, 36, 45, or 54. The implementation must ensure that the menu is configured
     * correctly for the returned slot count.
     *
     * @return The number of slots in the menu.
     */
    @Override
    public abstract int getSlots();
}
