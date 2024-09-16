package ru.piko.pikopluginlib.Items;

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
    }

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
     * @param item the item that needs to be edited | для предмета который надо редактировать
     */
    public ItemBuilder(@NotNull ItemStack item) {
        super(item);
    }

    /**
     * EN: <br>
     * Use it for `to()` from `AItemBuilder` <br>
     * Don't use this to create a clean instance! <br>
     * RU:<br>
     * Используйте для метода `to()` из `AItemBuilder` <br>
     * Не используёте это для создания чистого экземпляра!
     *
     * @see ItemBuilder#ItemBuilder(ItemStack)
     * @see ItemBuilder#ItemBuilder(Material)
     */
    public ItemBuilder() {}

    @Override
    public void init() {
        this.meta = this.item.getItemMeta();
    }

    @Override
    public void finish() {
        this.item.setItemMeta(this.meta);
    }

    public @NotNull ItemBuilder setDisplayName(@NotNull String name) {
        meta.setDisplayName(color(name));
        return this;
    }

    public @NotNull ItemBuilder setCustomModelData(int data) {
        meta.setCustomModelData(data);
        return this;
    }

    public @NotNull ItemBuilder setMaterial(@NotNull Material material) {
        item.setType(material);
        return this;
    }

    public @NotNull ItemBuilder setLore(@NotNull String... lore) {
        meta.setLore(color(Arrays.asList(lore)));
        return this;
    }

    public @NotNull ItemBuilder setLore(@NotNull List<String> lore) {
        meta.setLore(color(lore));
        return this;
    }

    public @NotNull ItemBuilder editLore(@NotNull Function<List<String>, List<String>> func) {
        List<String> lore = meta.getLore();
        meta.setLore(color(func.apply(lore)));
        return this;
    }

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

    @Deprecated
    public <T extends AItemBuilderModification> T modify(@NotNull T modification) {
        return (T) modification.modify(this);
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

    @Deprecated
    public @NotNull ItemMeta getMeta() {
        return meta;
    }
}
