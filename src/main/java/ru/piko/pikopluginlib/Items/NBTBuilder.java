package ru.piko.pikopluginlib.Items;

import de.tr7zw.nbtapi.*;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static de.tr7zw.nbtapi.NBTType.NBTTagList;

public class NBTBuilder {

    protected final NBTCompound compound;
    protected final NBTBuilder parentBuilder;

    /**
     * Creates a root NBTBuilder.
     *
     * @param compound The NBT compound.
     */
    public NBTBuilder(NBTCompound compound) {
        this.compound = compound;
        this.parentBuilder = null;
    }

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
     * @param parentBuilder The parent NBTBuilder.
     */
    public NBTBuilder(NBTCompound compound, NBTBuilder parentBuilder) {
        this.compound = compound;
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
        compound.setItemStackArray(key, items);
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

    public ItemStack[] getItemArray(String key) {
        return compound.getItemStackArray(key);
    }

    public List<ItemStack> getItemList(String key) {
        return List.of(getItemArray(key));
    }

    // Methods for nested objects
    public NBTBuilder getObject(String key) {
        NBTCompound subCompound = compound.getCompound(key);
        return subCompound != null ? new NBTBuilder(subCompound, this) : null;
    }

    public NBTBuilder getOrCreateObject(String key) {
        NBTCompound subCompound = compound.getCompound(key);
        if (subCompound == null) {
            return createObject(key);
        }
        return new NBTBuilder(subCompound, this);
    }

    public NBTBuilder createObject(String key) {
        NBTCompound subCompound = compound.addCompound(key);
        return new NBTBuilder(subCompound, this);
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

    public boolean hasArray(String key) {
        return compound.hasTag(key, NBTTagList);
    }

    public boolean hasObject(String key) {
        return compound.hasTag(key, NBTType.NBTTagCompound);
    }

    public boolean hasTag(String key, NBTType type) {
        return compound.hasTag(key, type);
    }

    public NBTCompound getCompound() {
        return compound;
    }

    public NBTBuilder getParentBuilder() {
        return parentBuilder;
    }

    // Glow effect methods

    /**
     * Adds the "glow" effect to the item by manipulating NBT data.
     * If the item already has real enchantments, the glow is considered permanent.
     *
     * @return The current NBTBuilder instance for further modifications.
     */
    public NBTBuilder addGlow() {
        if (!hasRealEnchantments()) {
            compound.mergeCompound(new NBTContainer("{Enchantments:[{id:\"\",lvl:0}]}"));
        }
        return this;
    }

    /**
     * Removes the "glow" effect from the item by manipulating NBT data.
     * If the item has real enchantments, the glow cannot be removed.
     *
     * @return The current NBTBuilder instance for further modifications.
     */
    public NBTBuilder removeGlow() {
        if (hasRealEnchantments()) {
            return this; // Can't remove glow if there are real enchantments
        }

        NBTCompoundList enchantments = compound.getCompoundList("Enchantments");
        List<ReadWriteNBT> newEnchantments = new ArrayList<>();

        for (ReadWriteNBT enchantment : enchantments) {
            String id = enchantment.getString("id");
            if (!id.isEmpty()) {
                newEnchantments.add(enchantment);
            }
        }

        compound.setObject("Enchantments", newEnchantments);
        return this;
    }

    /**
     * Checks if the item has the "glow" effect by analyzing NBT data.
     * The item glows if it has real enchantments or an empty enchantment entry.
     *
     * @return true if the item has the "glow" effect, false otherwise.
     */
    public boolean isGlow() {
        if (hasRealEnchantments()) {
            return true; // If there are real enchantments, the item is glowing
        }

        NBTCompoundList enchantments = compound.getCompoundList("Enchantments");
        for (ReadWriteNBT enchantment : enchantments) {
            String id = enchantment.getString("id");
            if (id.isEmpty()) {
                return true; // If there's an empty enchantment, the item is glowing
            }
        }

        return false;
    }

    /**
     * Toggles the "glow" effect on the item. If the item has real enchantments, the glow is considered permanent.
     * If the item has no real enchantments, the glow can be toggled.
     *
     * @return The current NBTBuilder instance for further modifications.
     */
    public NBTBuilder toggleGlow() {
        if (hasRealEnchantments()) {
            return this; // Can't toggle glow if there are real enchantments
        }

        if (isGlow()) {
            removeGlow();
        } else {
            addGlow();
        }
        return this;
    }

    /**
     * Checks if the item has real enchantments (non-empty id).
     *
     * @return true if the item has real enchantments, false otherwise.
     */
    public boolean hasRealEnchantments() {
        NBTCompoundList enchantments = compound.getCompoundList("Enchantments");
        for (ReadWriteNBT enchantment : enchantments) {
            String id = enchantment.getString("id");
            if (id != null && !id.isEmpty()) {
                return true; // Найдено настоящее зачарование
            }
        }
        return false; // Нет настоящих зачарований
    }
}
