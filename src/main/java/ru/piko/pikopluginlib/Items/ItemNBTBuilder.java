package ru.piko.pikopluginlib.Items;

import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class ItemNBTBuilder extends AItemBuilder {

    protected NBTItem nbtItem;
    protected NBTBuilder nbtBuilder;

    /**
     * EN: <br>
     * Use it to create a clean instance <br>
     * For the `to()` method from `AItemBuilder`, use a different constructor <br>
     * RU: <br>
     * Используйте для создания чистого экземпляра <br>
     * Для  метода `to()` из `AItemBuilder` используйте другой конструктор
     *
     * @see ItemNBTBuilder#ItemNBTBuilder()
     *
     * @param item the item that needs to be edited | для предмета который надо редактировать
     */
    public ItemNBTBuilder(@NotNull ItemStack item) {
        super(item);
        init();
    }

    /**
     * EN: <br>
     * Use it for `to()` from `AItemBuilder` <br>
     * Don't use this to create a clean instance! <br>
     * RU:<br>
     * Используйте для метода `to()` из `AItemBuilder` <br>
     * Не используёте это для создания чистого экземпляра!
     *
     * @see ItemNBTBuilder#ItemNBTBuilder(ItemStack)
     */
    public ItemNBTBuilder() {}

    @Override
    public void init() {
        this.nbtItem = new NBTItem(this.item);
        this.nbtBuilder = new NBTBuilder(nbtItem);
    }

    @Override
    public void finish() {
        nbtItem = (NBTItem) nbtBuilder.getCompound();
        this.item = nbtItem.getItem();
    }

    public @NotNull ItemNBTBuilder withNBT(Function<NBTBuilder, NBTBuilder> action) {
        nbtBuilder = action.apply(nbtBuilder);
        return this;
    }

    public @NotNull NBTBuilder getNBT() {
        return nbtBuilder;
    }

    public boolean has(String key) {
        return nbtBuilder.has(key);
    }

    public boolean hasObject(String key) {
        return nbtBuilder.hasObject(key);
    }

    public boolean hasTag(String key, NBTType type) {
        return nbtBuilder.hasTag(key, type);
    }

}
