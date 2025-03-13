package ru.piko.pikopluginlib.PlayersData

abstract class APersistentPlayerData : APlayerData() {
    abstract fun saveData() // Метод для сохранения данных
    abstract fun loadData() // Метод для загрузки данных
}