package ru.piko.pikopluginlib.MenuSystem;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.piko.pikopluginlib.Items.ItemBuilder;
import ru.piko.pikopluginlib.PlayersData.PlayerData;

public abstract class PaginatedMenu extends Menu {

    protected int page = 0;
    protected int maxItemsPerPage;
    protected int index = 0;

    // New parameters for configuring the menu layout
    protected boolean hasVerticalColumns = true;
    protected boolean hasTopRow = true;

    public PaginatedMenu(@NotNull PlayerData playerData) {
        super(playerData);
    }

    /**
     * Configures whether the menu should display vertical columns.
     *
     * @param hasVerticalColumns true to display vertical columns, false otherwise.
     */
    public void setVerticalColumns(boolean hasVerticalColumns) {
        this.hasVerticalColumns = hasVerticalColumns;
    }

    /**
     * Configures whether the menu should display the top row.
     *
     * @param hasTopRow true to display the top row, false otherwise.
     */
    public void setTopRow(boolean hasTopRow) {
        this.hasTopRow = hasTopRow;
    }

    @Override
    public void open() {
        inventory = Bukkit.createInventory(this, getSlots(), getMenuName());

        this.setMenuItems();
        this.playerData.getOwner().sendMessage(String.valueOf(maxItemsPerPage));

        playerData.getOwner().openInventory(inventory);
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
                if (inventory.getItem(i) == null) {
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
        this.calculateMaxItemsPerPage();
    }

    /**
     * Calculates the maximum number of items that can be displayed per page based on
     * the current inventory size and layout configuration.
     */
    private void calculateMaxItemsPerPage() {
        if (inventory == null) {
            maxItemsPerPage = getSlots()-9;
            return;
        }
        int slots = getSlots();
        int emptySlots = 0;

        // Iterate through each slot
        for (int i = 0; i < slots; i++) {
            // Check if the slot is empty
            if (inventory.getItem(i) == null || inventory.getItem(i).getType() == Material.AIR) {
                emptySlots++;
            }
        }

        // Set the maximum items per page to the number of empty slots
        maxItemsPerPage = emptySlots;
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
