package ru.piko.pikopluginlib.Crafts;

import org.bukkit.NamespacedKey;

public abstract class ARecipeBuilder {

    protected NamespacedKey key;

    public abstract String getType();

    public ARecipeBuilder(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
