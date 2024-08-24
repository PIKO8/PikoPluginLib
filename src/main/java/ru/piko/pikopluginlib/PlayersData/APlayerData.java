package ru.piko.pikopluginlib.PlayersData;

import org.bukkit.entity.Player;

public abstract class APlayerData {

    public PlayerData data;

    public APlayerData(PlayerData data) {
        this.data = data;
    }

    public Player getPlayer() {
        return data.getOwner();
    }

    public abstract String getId();
}
