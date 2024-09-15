package ru.piko.pikopluginlib.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;
import ru.piko.pikopluginlib.Main;
import ru.piko.pikopluginlib.MenuSystem.Menu;
import ru.piko.pikopluginlib.PlayersData.PlayerData;

public class MenuEvent implements Listener {

    @EventHandler
    public void onClickMenu(InventoryClickEvent e) {
        if (e == null) return;
        if (e.getClickedInventory() == null) { return; }
        InventoryHolder holder = e.getClickedInventory().getHolder();
        if(holder instanceof Menu menu) {
            Player p = (Player) e.getWhoClicked();
            e.setCancelled(true);
            if (e.getCurrentItem() != null && e.getCurrentItem().getItemMeta() != null && e.getCurrentItem().getItemMeta().hasCustomModelData()) {
                PlayerData playerData = Main.getPlugin().getPlayerData(p.getUniqueId());
                switch (e.getCurrentItem().getItemMeta().getCustomModelData()) {
                    //case 98 -> ;
                    case 99 -> p.closeInventory();
                }
            }
            menu.clickMenu(e);
        }
    }

    @EventHandler
    public void onCloseMenu(InventoryCloseEvent e) {
        if (e == null) return;
        if(e.getInventory().getHolder() instanceof Menu menu) {
            menu.closeMenu(e);
        }
    }

}
