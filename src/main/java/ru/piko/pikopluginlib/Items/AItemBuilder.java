package ru.piko.pikopluginlib.Items;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

@Deprecated(forRemoval = true)
public abstract class AItemBuilder<T extends AItemBuilder<T>> {

    protected ItemStack item;
    protected ItemMeta meta;

    protected AItemBuilder(@NotNull ItemStack item) {
        this.item = item;
        this.meta = item.getItemMeta();
    }

    protected AItemBuilder(@NotNull ItemStack item, @NotNull ItemMeta meta) {
        this.item = item;
        this.meta = meta;
    }

    protected AItemBuilder() {}

    public T self() {
        return (T) this;
    }

    public <U extends AItemBuilder> U to(@NotNull U builder) {
        finish();
        builder.setItem(item);
        meta = meta == null ? Bukkit.getItemFactory().getItemMeta(this.item.getType()) : meta;
        builder.setMeta(meta);
        builder.init();
        return builder;
    }

    public T conf(Function<T, T> function) {
        try {
            return function.apply(self());
        } catch (Exception e) {
            e.printStackTrace();
            return this.self();
        }
    }

    public T configure(Function<T, T> function) {
        return conf(function);
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