package ru.piko.pikopluginlib.Items;

import de.tr7zw.nbtapi.*;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @deprecated Using ItemNBTBuilder
 */
@Deprecated
public class ItemBuilderNBT extends AItemBuilderModification {

    private NBTItem nbtItem;
    private NBTBuilder nbtBuilder;

    @Override
    public void onModify() {
        ItemStack item = builder.build();
        this.nbtItem = new NBTItem(item);
        this.nbtBuilder = new NBTBuilder(nbtItem);
    }

    @Override
    public void onExitModify() {
        nbtItem = (NBTItem) nbtBuilder.getCompound();
        builder = new ItemBuilder(nbtItem.getItem());
    }

    /**
     * @deprecated Using ItemNBTBuilder
     */
    @Deprecated
    public ItemBuilderNBT withNBT(Function<NBTBuilder, NBTBuilder> action) {
        nbtBuilder = action.apply(nbtBuilder);
        return this;
    }

    /**
     * @deprecated Using ItemNBTBuilder
     */
    @Deprecated
    public NBTBuilder getNBT() {
        return nbtBuilder;
    }

    /**
     * @deprecated Using ItemNBTBuilder
     */
    @Deprecated
    public boolean has(String key) {
        return nbtBuilder.has(key);
    }

    /**
     * @deprecated Using ItemNBTBuilder
     */
    @Deprecated
    public boolean hasObject(String key) {
        return nbtBuilder.hasObject(key);
    }

    /**
     * @deprecated Using ItemNBTBuilder
     */
    @Deprecated
    public boolean hasTag(String key, NBTType type) {
        return nbtBuilder.hasTag(key, type);
    }
}
