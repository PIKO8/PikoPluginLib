package ru.piko.pikopluginlib.PlayersData;

public interface IPersistentPlayerData extends IPlayerData {
    // Интерфейс для персистентных данных
    void saveData(); // Метод для сохранения данных
    void loadData(); // Метод для загрузки данных
}
