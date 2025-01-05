package ru.piko.pikopluginlib.Items;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static ru.piko.pikopluginlib.Utils.UText.color;

public class ItemBuilder extends AItemBuilder<ItemBuilder> {

    /**
     * EN: <br>
     * Use it to create a clean instance <br>
     * For the `to()` method from `AItemBuilder`, use a different constructor <br>
     * RU: <br>
     * Используйте для создания чистого экземпляра <br>
     * Для  метода `to()` из `AItemBuilder` используйте другой конструктор
     *
     * @see ItemBuilder#ItemBuilder()
     *
     * @param material the item that needs to be edited | для предмета который надо редактировать
     */
    public ItemBuilder(@NotNull Material material) {
        super(new ItemStack(material));
        init();
    }

    /**
     * EN: <br>
     * Use it to create a clean instance <br>
     * For the {@link AItemBuilder#to(AItemBuilder)} method use a different constructor <br>
     * RU: <br>
     * Используйте для создания чистого экземпляра <br>
     * Для  метода `{@link AItemBuilder#to(AItemBuilder)} используйте другой конструктор
     *
     * @see ItemBuilder#ItemBuilder()
     *
     * @param item the item that needs to be edited | для предмета который надо редактировать
     */
    public ItemBuilder(@NotNull ItemStack item) {
        super(item);
    }

    /**
     * EN: <br>
     * Use it for {@link AItemBuilder#to(AItemBuilder)} <br>
     * Don't use this to create a clean instance! <br>
     * RU:<br>
     * Используйте для метода {@link AItemBuilder#to(AItemBuilder)} <br>
     * Не используёте это для создания чистого экземпляра!
     *
     * @see ItemBuilder#ItemBuilder(ItemStack)
     * @see ItemBuilder#ItemBuilder(Material)
     */
    public ItemBuilder() {}

    @Override
    public void init() {
        this.meta = this.item.getItemMeta() == null ? Bukkit.getItemFactory().getItemMeta(this.item.getType()) : this.item.getItemMeta();
    }

    @Override
    public void finish() {
        this.item.setItemMeta(this.meta);
    }



    public @NotNull ItemBuilder setCustomModelData(int data) {
        meta.setCustomModelData(data);
        return this;
    }

    public @NotNull ItemBuilder setMaterial(@NotNull Material material) {
        item.setType(material);
        return this;
    }

    // <editor-fold desc="Deprecated name & lore">
    /**
     * @deprecated see {@link #displayName(Component)}
     */
    @Deprecated
    public @NotNull ItemBuilder setDisplayName(@NotNull String name) {
        meta.setDisplayName(color(name));
        return this;
    }

    /**
     * @deprecated see {@link #lore(Component...)}
     */
    @Deprecated
    public @NotNull ItemBuilder setLore(@NotNull String... lore) {
        meta.setLore(color(Arrays.asList(lore)));
        return this;
    }

    /**
     * @deprecated see {@link #lore(List)}
     */
    @Deprecated
    public @NotNull ItemBuilder setLore(@NotNull List<String> lore) {
        meta.setLore(color(lore));
        return this;
    }

    /**
     * @deprecated see {@link #lore(Function)}
     */
    @Deprecated
    public @NotNull ItemBuilder editLore(@NotNull Function<List<String>, List<String>> func) {
        List<String> lore = meta.getLore();
        meta.setLore(color(func.apply(lore)));
        return this;
    }
    // </editor-fold>

    // <editor-fold desc="Component name & lore">
    public @NotNull ItemBuilder displayName(@NotNull Component name) {
        meta.displayName(name);
        return this;
    }

    public @NotNull ItemBuilder lore(@NotNull Component... lore) {
        meta.lore(Arrays.asList(lore));
        return this;
    }

    public @NotNull ItemBuilder lore(@NotNull List<Component> lore) {
        meta.lore(lore);
        return this;
    }

    public @NotNull ItemBuilder lore(@NotNull Function<List<Component>, List<Component>> func) {
        List<Component> lore = meta.lore();
        meta.lore(func.apply(lore));
        return this;
    }
    // </editor-fold>

    public @NotNull ItemBuilder addEnchantment(@NotNull Enchantment enchantment, int level) {
        meta.addEnchant(enchantment, level, true);
        return this;
    }

    public @NotNull ItemBuilder addItemFlags(@NotNull ItemFlag... flags) {
        meta.addItemFlags(flags);
        return this;
    }

    public @NotNull ItemBuilder setUnbreakable(boolean unbreakable) {
        meta.setUnbreakable(unbreakable);
        return this;
    }

    public @NotNull ItemBuilder setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public @NotNull ItemBuilder addAttributeModifier(@NotNull Attribute attribute, @NotNull AttributeModifier modifier) {
        meta.addAttributeModifier(attribute, modifier);
        return this;
    }

    public @NotNull ItemBuilder setDamage(int damage) {
        if (item.getType().getMaxDurability() > 0) {
            ((org.bukkit.inventory.meta.Damageable) meta).setDamage(damage);
        }
        return this;
    }

    public @NotNull ItemBuilder copy() {
        return new ItemBuilder(this.build().clone());
    }

    public @NotNull ItemBuilder clearEnchantments() {
        meta.getEnchants().keySet().forEach(meta::removeEnchant);
        return this;
    }
}
