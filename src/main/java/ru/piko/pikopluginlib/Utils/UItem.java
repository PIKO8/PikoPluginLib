package ru.piko.allpikoplugin.Utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

import static ru.piko.allpikoplugin.Utils.UText.*;

public class UItem {

    public static ItemStack makeItem(Material material, String displayName, int customModelData, String... lore) {

        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(color(displayName));
        itemMeta.setCustomModelData(customModelData);
        itemMeta.setLore(color(Arrays.asList(lore)));
        item.setItemMeta(itemMeta);

        return item;
    }
}
