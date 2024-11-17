package ru.piko.pikopluginlib.PlayersData;

public abstract class APersistentPlayerData extends APlayerData {

    public APersistentPlayerData(PlayerData data) {
        super(data);
    }
    public abstract void saveData(); // Метод для сохранения данных
    public abstract void loadData(); // Метод для загрузки данных
}
