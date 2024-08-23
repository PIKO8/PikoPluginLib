package ru.piko.pikopluginlib.Items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

import static ru.piko.pikopluginlib.Utils.UText.color;

public class ItemBuilder {

    /**
     * The ItemStack being built by this ItemBuilder.
     */
    private final ItemStack item;

    /**
     * The ItemMeta associated with the ItemStack, used to modify its properties.
     */
    private final ItemMeta meta;

    /**
     * Constructs an ItemBuilder with the specified Material.
     *
     * @param material The material for the ItemStack.
     */
    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
        this.meta = item.getItemMeta();
    }

    /**
     * Constructs an ItemBuilder with an existing ItemStack.
     *
     * @param item The ItemStack to modify.
     */
    public ItemBuilder(ItemStack item) {
        this.item = item;
        this.meta = item.getItemMeta();
    }

    /**
     * Sets the display name of the item.
     *
     * @param name The new display name, with color codes supported.
     * @return The current ItemBuilder instance for chaining.
     */
    public ItemBuilder setDisplayName(String name) {
        meta.setDisplayName(color(name));
        return this;
    }

    /**
     * Sets the custom model data for the item.
     *
     * @param data The custom model data value.
     * @return The current ItemBuilder instance for chaining.
     */
    public ItemBuilder setCustomModelData(int data) {
        meta.setCustomModelData(data);
        return this;
    }

    /**
     * Sets the lore (description) of the item.
     *
     * @param lore The lines of the lore, with color codes supported.
     * @return The current ItemBuilder instance for chaining.
     */
    public ItemBuilder setLore(String... lore) {
        meta.setLore(color(Arrays.asList(lore)));
        return this;
    }

    /**
     * Sets the lore (description) of the item.
     *
     * @param lore The list of lore lines, with color codes supported.
     * @return The current ItemBuilder instance for chaining.
     */
    public ItemBuilder setLore(List<String> lore) {
        meta.setLore(color(lore));
        return this;
    }

    /**
     * Applies a custom modification to the item using the provided AItemBuilderModification.
     *
     * @param <T> The type of the modification.
     * @param modification The modification to apply.
     * @return The modification instance, allowing for further customization.
     */
    public <T extends AItemBuilderModification> T modify(T modification) {
        return (T) modification.modify(this);
    }

    /**
     * Finalizes the item by applying the modified ItemMeta to the ItemStack.
     *
     * @return The fully constructed ItemStack.
     */
    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Returns the ItemStack being built or modified.
     *
     * @return The ItemStack associated with this builder.
     */
    public ItemStack getItem() {
        return item;
    }

    /**
     * Returns the ItemMeta of the ItemStack, used for further modifications.
     *
     * @return The ItemMeta associated with the current ItemStack.
     */
    public ItemMeta getMeta() {
        return meta;
    }
}
