package ru.piko.pikopluginlib.Items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static ru.piko.pikopluginlib.Utils.UText.color;

public class ItemBuilder {

    private final ItemStack item;
    private final ItemMeta meta;

    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
        this.meta = item.getItemMeta();
    }

    public ItemBuilder(ItemStack item) {
        this.item = item;
        this.meta = item.getItemMeta();
    }

    public ItemBuilder setDisplayName(String name) {
        meta.setDisplayName(color(name));
        return this;
    }

    public ItemBuilder setCustomModelData(int data) {
        meta.setCustomModelData(data);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        meta.setLore(color(Arrays.asList(lore)));
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        meta.setLore(color(lore));
        return this;
    }

    public ItemBuilder editLore(Function<List<String>, List<String>> func) {
        List<String> lore = meta.getLore();
        meta.setLore(color(func.apply(lore)));
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        meta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder addItemFlags(ItemFlag... flags) {
        meta.addItemFlags(flags);
        return this;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        meta.setUnbreakable(unbreakable);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemBuilder addAttributeModifier(Attribute attribute, AttributeModifier modifier) {
        meta.addAttributeModifier(attribute, modifier);
        return this;
    }

    public <T extends AItemBuilderModification> T modify(T modification) {
        return (T) modification.modify(this);
    }

    public ItemBuilder setDamage(int damage) {
        if (item.getType().getMaxDurability() > 0) {
            ((org.bukkit.inventory.meta.Damageable) meta).setDamage(damage);
        }
        return this;
    }

    public ItemBuilder copy() {
        return new ItemBuilder(this.build().clone());
    }

    public ItemBuilder clearEnchantments() {
        meta.getEnchants().keySet().forEach(meta::removeEnchant);
        return this;
    }

    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }

    public ItemMeta getMeta() {
        return meta;
    }
}
