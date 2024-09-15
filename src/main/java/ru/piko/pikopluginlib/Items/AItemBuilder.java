package ru.piko.pikopluginlib.Items;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public abstract class AItemBuilder<T extends AItemBuilder<T>> {

    protected ItemStack item;
    protected ItemMeta meta;

    protected AItemBuilder(@NotNull ItemStack item) {
        this.item = item;
        this.meta = item.getItemMeta();
        init();
    }

    public AItemBuilder(@NotNull ItemStack item, @NotNull ItemMeta meta) {
        this.item = item;
        this.meta = meta;
        init();
    }

    public AItemBuilder() {}

    public <U extends AItemBuilder<U>> U to(@NotNull U builder) {
        finish();
        builder.setItem(item);
        builder.setMeta(meta);
        builder.init();
        return builder;
    }

    public ItemStack build() {
        finish();
        return item;
    }

    protected void setItem(@NotNull ItemStack item) {
        this.item = item;
    }

    protected void setMeta(@NotNull ItemMeta meta) {
        this.meta = meta;
    }

    public abstract void init();
    public abstract void finish();
}