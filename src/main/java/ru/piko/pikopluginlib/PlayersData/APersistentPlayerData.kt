package ru.piko.pikopluginlib.PlayersData

abstract class APersistentPlayerData(data: PlayerData) : APlayerData(data) {
    abstract fun saveData() // Метод для сохранения данных
    abstract fun loadData() // Метод для загрузки данных
}