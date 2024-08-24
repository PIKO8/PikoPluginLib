package ru.piko.pikopluginlib.Items;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class NBTBuilder {

    private final NBTCompound compound;
    private final NBTBuilder parentBuilder;

    /**
     * Creates a root NBTBuilder.
     *
     * @param item The NBTItem to modify.
     */
    public NBTBuilder(NBTItem item) {
        this.compound = item;
        this.parentBuilder = null;
    }

    /**
     * Creates a nested NBTBuilder for a sub-compound.
     *
     * @param compound The parent NBT compound.
     * @param key      The key of the new NBT sub-compound.
     * @param parentBuilder The parent NBTBuilder.
     */
    public NBTBuilder(NBTCompound compound, String key, NBTBuilder parentBuilder) {
        this.compound = compound.addCompound(key);
        this.parentBuilder = parentBuilder;
    }

    public NBTBuilder setInteger(String key, int value) {
        compound.setInteger(key, value);
        return this;
    }

    public NBTBuilder setString(String key, String value) {
        compound.setString(key, value);
        return this;
    }

    public NBTBuilder setBoolean(String key, boolean value) {
        compound.setBoolean(key, value);
        return this;
    }

    // Methods for working with nested objects
    public NBTBuilder createObject(String key) {
        return new NBTBuilder(compound, key, this);
    }

    /**
     * Closes the current NBT compound and returns to the parent compound.
     *
     * @return The parent NBTBuilder instance, or null if this is the root.
     */
    public NBTBuilder closeObject() {
        return parentBuilder;
    }

    public NBTBuilder setItem(String key, ItemStack item) {
        compound.setItemStack(key, item);
        return this;
    }

    public ItemStack getItem(String key) {
        return compound.getItemStack(key);
    }

    public NBTBuilder setItemArray(String key, ItemStack[] items) {
        for (int i = 0; i < items.length; i++) {
            compound.addCompound(key + "_" + i).setItemStack("item", items[i]);
        }
        return this;
    }

    public ItemStack[] getItemArray(String key, int length) {
        ItemStack[] items = new ItemStack[length];
        for (int i = 0; i < length; i++) {
            items[i] = compound.getCompound(key + "_" + i).getItemStack("item");
        }
        return items;
    }

    public NBTBuilder setItemList(String key, List<ItemStack> items) {
        return setItemArray(key, items.toArray(new ItemStack[0]));
    }

    public List<ItemStack> getItemList(String key, int length) {
        return List.of(getItemArray(key, length));
    }
}