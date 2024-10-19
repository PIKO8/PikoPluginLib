package ru.piko.pikopluginlib.MenuSystem

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import ru.piko.pikopluginlib.Items.ItemBuilder
import ru.piko.pikopluginlib.PlayersData.PlayerData

abstract class PaginatedMenu(playerData: PlayerData) : Menu(playerData) {

    protected var page = 0
    protected var maxItemsPerPage: Int = 0
    protected var index = 0

    // New parameters for configuring the menu layout
    protected var hasVerticalColumns = true
    protected var hasTopRow = true

    /**
     * Configures whether the menu should display vertical columns.
     *
     * @param hasVerticalColumns true to display vertical columns, false otherwise.
     */
    fun setVerticalColumns(hasVerticalColumns: Boolean) {
        this.hasVerticalColumns = hasVerticalColumns
    }

    /**
     * Configures whether the menu should display the top row.
     *
     * @param hasTopRow true to display the top row, false otherwise.
     */
    fun setTopRow(hasTopRow: Boolean) {
        this.hasTopRow = hasTopRow
    }

    /**
     * Adds borders and navigation items to the menu.
     *
     * @param fillerItem The item to be used as filler, or null to use the default glass pane.
     */
    fun addMenuBorder(fillerItem: ItemStack?) {
        val slots = getSlots()
        val filler = fillerItem ?: super.FILLER_GLASS

        // Navigation buttons
        inventory.setItem(slots - 6, ItemBuilder(Material.DARK_OAK_BUTTON).setDisplayName("&2<--").setCustomModelData(198).build())
        inventory.setItem(slots - 1, BARRIER_CLOSE)
        inventory.setItem(slots - 4, ItemBuilder(Material.DARK_OAK_BUTTON).setDisplayName("&2-->").setCustomModelData(199).build())

        // Top row filler
        if (hasTopRow) {
            for (i in 0 until 9.coerceAtMost(slots)) {
                if (inventory.getItem(i) == null) {
                    inventory.setItem(i, filler)
                }
            }
        }

        // Vertical columns filler
        if (hasVerticalColumns) {
            for (i in 9 until slots step 9) {
                if (inventory.getItem(i) == null) {
                    inventory.setItem(i, filler)
                }
                if (i + 8 < slots && inventory.getItem(i + 8) == null) {
                    inventory.setItem(i + 8, filler)
                }
            }
        }

        // Bottom row filler
        for (i in slots - 9 until slots) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, filler)
            }
        }
        this.calculateMaxItemsPerPage()
    }

    /**
     * Calculates the maximum number of items that can be displayed per page based on
     * the current inventory size and layout configuration.
     */
    private fun calculateMaxItemsPerPage() {
        if (!isInventoryInitialized()) {
            maxItemsPerPage = getSlots() - 9
            return
        }
        val slots = getSlots()
        var emptySlots = 0
        
        // Iterate through each slot
        for (i in 0 until slots) {
            // Check if the slot is empty
            if (inventory.getItem(i) == null || inventory.getItem(i)?.type == Material.AIR) {
                emptySlots++
            }
        }
        
        // Set the maximum items per page to the number of empty slots
        maxItemsPerPage = emptySlots
    }
	
    /**
     * Gets the number of slots in the menu. This method must return one of the following
     * values: 27, 36, 45, or 54. The implementation must ensure that the menu is configured
     * correctly for the returned slot count.
     *
     * @return The number of slots in the menu.
     */
    abstract override fun getSlots(): Int
}