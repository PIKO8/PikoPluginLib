package ru.piko.pikopluginlib.MenuSystem;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.piko.pikopluginlib.Items.ItemBuilder;
import ru.piko.pikopluginlib.PlayersData.PlayerData;

import static ru.piko.pikopluginlib.Utils.UItem.makeItem;

public abstract class Menu implements InventoryHolder {

    protected Inventory inventory;
    protected PlayerData playerData;
    protected ItemStack FILLER_GLASS = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").setCustomModelData(190).build();
    protected ItemStack FILLER_LIGHT_GLASS = new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE).setDisplayName(" ").setCustomModelData(190).build();
    protected ItemStack ARROW_BACK = new ItemBuilder(Material.ARROW).setDisplayName("&cНазад").setCustomModelData(98).build();
    protected ItemStack BARRIER_CLOSE = new ItemBuilder(Material.BARRIER).setDisplayName("&cЗакрыть").setCustomModelData(99).build();
    public Menu(PlayerData playerData) {
        this.playerData = playerData;
    }

    public abstract String getMenuName();
    public abstract int getSlots();
    public abstract void setMenuItems();
    public abstract void handleMenu(InventoryClickEvent e);

    public void open() {
        inventory = Bukkit.createInventory(this, getSlots(), getMenuName());
        
        this.setMenuItems();

        playerData.getOwner().openInventory(inventory);
    }

    public void setFillerGlass(){
        for (int i = 0; i < getSlots(); i++) {
            if (inventory.getItem(i) == null){
                inventory.setItem(i, FILLER_GLASS);
            }
        }
    }

    public void setFillerLightGlass() {
        for (int i = 0; i < getSlots(); i++) {
            if (inventory.getItem(i) == null){
                inventory.setItem(i, FILLER_LIGHT_GLASS);
            }
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
