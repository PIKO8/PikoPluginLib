package ru.piko.pikopluginlib.Items;

/**
 * @deprecated Using AItemBuilder
 */
@Deprecated
public abstract class AItemBuilderModification {

    public ItemBuilder builder;

    public AItemBuilderModification modify(ItemBuilder builder) {
        this.builder = builder;
        onModify();
        return this;
    }

    public abstract void onModify();

    public ItemBuilder exitModify() {
        onExitModify();
        return builder;
    }

    public abstract void onExitModify();

}
