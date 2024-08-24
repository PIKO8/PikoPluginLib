package ru.piko.pikopluginlib.Items;

import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTType;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class ItemBuilderNBT extends AItemBuilderModification {

    private NBTItem nbtItem;
    private NBTBuilder nbtBuilder;

    @Override
    public void onModify() {
        ItemStack item = builder.getItem();
        this.nbtItem = new NBTItem(item);
        this.nbtBuilder = new NBTBuilder(nbtItem);
    }

    @Override
    public void onExitModify() {
        ItemStack item = builder.getItem();
        item.setItemMeta(nbtItem.getItem().getItemMeta());
    }

    /**
     * Provides access to the NBT data for modification using a lambda.
     *
     * @param action The lambda that accepts an NBTBuilder to perform operations.
     * @return The current ItemBuilderNBT instance for further modifications.
     */
    public ItemBuilderNBT withNBT(Consumer<NBTBuilder> action) {
        action.accept(nbtBuilder);
        return this;
    }

    /**
     * Returns the NBTBuilder instance for direct access.
     *
     * @return The NBTBuilder associated with this modification.
     */
    public NBTBuilder getNBT() {
        return nbtBuilder;
    }

    /**
     * Checks if the given NBT key exists.
     *
     * @param key The key to check in the NBT data.
     * @return True if the key exists, false otherwise.
     */
    public boolean has(String key) {
        return nbtBuilder.has(key);
    }

    /**
     * Checks if the given NBT object exists.
     *
     * @param key The key of the object to check.
     * @return True if the object exists, false otherwise.
     */
    public boolean hasObject(String key) {
        return nbtBuilder.hasObject(key);
    }

    public boolean hasTag(String key, NBTType type) {
        return nbtBuilder.hasTag(key, type);
    }
}
