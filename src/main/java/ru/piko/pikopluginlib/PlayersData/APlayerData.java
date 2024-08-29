package ru.piko.pikopluginlib.PlayersData;

import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public abstract class APlayerData {

    public PlayerData data;

    public APlayerData(PlayerData data) {
        this.data = data;
    }

    public Player getPlayer() {
        return data.getOwner();
    }

    public abstract String getId();


    @SuppressWarnings("unchecked")
    public <T> T getVariable(String name, Class<T> type) {
        try {
            Field field = this.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return (T) field.get(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> void setVariable(String name, T value) {
        try {
            Field field = this.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(this, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
