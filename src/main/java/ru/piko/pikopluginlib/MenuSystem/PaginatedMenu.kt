package ru.piko.pikopluginlib.MenuSystem

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import ru.piko.pikopluginlib.Items.ItemBuilder
import ru.piko.pikopluginlib.PlayersData.PlayerData

abstract class PaginatedMenu(playerData: PlayerData) : Menu(playerData) {

    protected var page = 0
    protected var maxItemsPerPage: Int = 0
    protected var index = 0

    var hasVerticalColumns = true
    var hasTopRow = true
    var hasNavigationButtons = true

    /**
     * Adds borders and navigation items to the menu.
     *
     * @param fillerItem The item to be used as filler, or null to use the default glass pane.
     */
    fun addMenuBorder(fillerItem: ItemStack? = null) {
        val slots = getSlots()
        val thisFiller = fillerItem ?: MenuItems.FILLER_GLASS

        // Navigation buttons
        if (hasNavigationButtons) {
            inventory.setItem(slots - 6, ItemBuilder(Material.DARK_OAK_BUTTON).setDisplayName("&2<--").setCustomModelData(198).build())
            inventory.setItem(slots - 1, MenuItems.BARRIER_CLOSE)
            inventory.setItem(slots - 4, ItemBuilder(Material.DARK_OAK_BUTTON).setDisplayName("&2-->").setCustomModelData(199).build())
        }

        // Top row filler
        if (hasTopRow) {
            for (i in 0 until 9.coerceAtMost(slots)) {
                if (inventory.getItem(i) == null) {
                    inventory.setItem(i, thisFiller)
                }
            }
        }

        // Vertical columns filler
        if (hasVerticalColumns) {
            for (i in 9 until slots step 9) {
                if (inventory.getItem(i) == null) {
                    inventory.setItem(i, thisFiller)
                }
                if (i + 8 < slots && inventory.getItem(i + 8) == null) {
                    inventory.setItem(i + 8, thisFiller)
                }
            }
        }

        // Bottom row filler
        for (i in slots - 9 until slots) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, thisFiller)
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