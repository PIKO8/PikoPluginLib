package ru.piko.pikopluginlib.MenuSystem;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import ru.piko.pikopluginlib.Items.ItemBuilder;
import ru.piko.pikopluginlib.PlayersData.PlayerData;

import static ru.piko.pikopluginlib.Utils.UItem.makeItem;

public abstract class PaginatedMenu extends Menu {

    protected int page = 0;
    protected int maxItemsPerPage = 28;
    protected int index = 0;


    public PaginatedMenu(PlayerData playerData) {
        super(playerData);
    }

    public void addMenuBorder(){
        inventory.setItem(48, new ItemBuilder(Material.DARK_OAK_BUTTON).setDisplayName("&2<--").setCustomModelData(198).build());

        inventory.setItem(53, BARRIER_CLOSE);

        inventory.setItem(50, new ItemBuilder(Material.DARK_OAK_BUTTON).setDisplayName("&2-->").setCustomModelData(199).build());

        for (int i = 0; i < 10; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, super.FILLER_GLASS);
            }
        }

        inventory.setItem(17, super.FILLER_GLASS);
        inventory.setItem(18, super.FILLER_GLASS);
        inventory.setItem(26, super.FILLER_GLASS);
        inventory.setItem(27, super.FILLER_GLASS);
        inventory.setItem(35, super.FILLER_GLASS);
        inventory.setItem(36, super.FILLER_GLASS);

        for (int i = 44; i < 54; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, super.FILLER_GLASS);
            }
        }
    }

    public int getMaxItemsPerPage() {
        return maxItemsPerPage;
    }

}
