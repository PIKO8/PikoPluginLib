package ru.piko.pikopluginlib.Items;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTType;
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

    // Methods for setting data
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

    public NBTBuilder setItem(String key, ItemStack item) {
        compound.setItemStack(key, item);
        return this;
    }

    public NBTBuilder setItemArray(String key, ItemStack[] items) {
        for (int i = 0; i < items.length; i++) {
            compound.addCompound(key + "_" + i).setItemStack("item", items[i]);
        }
        return this;
    }

    public NBTBuilder setItemList(String key, List<ItemStack> items) {
        return setItemArray(key, items.toArray(new ItemStack[0]));
    }

    // Methods for getting data
    public int getInteger(String key) {
        return compound.getInteger(key);
    }

    public String getString(String key) {
        return compound.getString(key);
    }

    public boolean getBoolean(String key) {
        return compound.getBoolean(key);
    }

    public ItemStack getItem(String key) {
        return compound.getItemStack(key);
    }

    public ItemStack[] getItemArray(String key, int length) {
        ItemStack[] items = new ItemStack[length];
        for (int i = 0; i < length; i++) {
            items[i] = compound.getCompound(key + "_" + i).getItemStack("item");
        }
        return items;
    }

    public List<ItemStack> getItemList(String key, int length) {
        return List.of(getItemArray(key, length));
    }

    // Methods for nested objects
    public NBTBuilder getObject(String key) {
        NBTCompound subCompound = compound.getCompound(key);
        return subCompound != null ? new NBTBuilder(subCompound, key, this) : null;
    }

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

    // Methods for checking existence
    public boolean has(String key) {
        return compound.hasTag(key);
    }

    public boolean hasObject(String key) {
        return compound.hasTag(key, NBTType.NBTTagCompound);
    }

    public boolean hasTag(String key, NBTType type) {
        return compound.hasTag(key, type);
    }
}