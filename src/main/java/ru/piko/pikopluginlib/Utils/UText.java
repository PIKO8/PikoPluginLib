package ru.piko.pikopluginlib.Utils;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class UText {

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static List<String> color(List<String> list) {
        List<String> result = new ArrayList<>();
        for (String string: list) {
            result.add(color(string));
        }
        return result;
    }


    /**
     * RU: Возвращает компонент, представляющий название элемента, с учетом его отображаемого имени, ключа перевода и цвета редкости. <br>
     * EN: Returns a Component representing the item's name, taking into account its display name, translation key, and rarity color. <br> <br>
     *
     * @param item the ItemStack to generate the component for
     * @return a Component representing the item's name
     */
    public static Component itemNameComponent(ItemStack item) {
        Component component;
        if (item.getItemMeta() != null && item.getItemMeta().hasDisplayName() && !item.displayName().toString().isEmpty()) {
            component = item.displayName();
        } else {
            String translationKey = item.getType().getItemTranslationKey();
            Component itemNameComponent;
            if (translationKey != null) {
                itemNameComponent = Component.translatable(translationKey);
            } else {
                itemNameComponent = Component.text(item.getType().name().toLowerCase());
            }
            itemNameComponent = itemNameComponent.color(item.getItemMeta().getRarity().color());
            component = itemNameComponent;
        }
        component = component.hoverEvent(item);
        return component;
    }
}
