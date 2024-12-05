# Описание |  Description

RU: <br>
Моя библиотека для плагинов майнкрафт для удобства программирования.<br>
Часть была взята с YouTube с канала [Kodi Simpson](https://www.youtube.com/@KodySimpson) ([плейлист про spigot](https://www.youtube.com/playlist?list=PLfu_Bpi_zcDNEKmR82hnbv9UxQ16nUBF7)) и переделана под  себя.<br>
<br>EN:<br>
My library for minecraft plugins for ease of programming.<br>
The part was taken from the YouTube channel [Kodi Simpson](https://www.youtube.com/@KodySimpson) ([playlist about spigot](https://www.youtube.com/playlist?list=PLfu_Bpi_zcDNEKmR82hnbv9UxQ16nUBF7)) and remade for yourself.<br>

# Релизы | Release
RU:
Формат файла: <br>
`PikoPluginLib-Группа-ВерсияМайнкрафта-ВерсияПлагина.jar`<br>
## Группы
Группы сделанны для того что бы сделать разнообразие варинтов jar что бы исключить ошибки о одинаковых библиотеках в разных плагинах например 2 разных jar с котлином и без
### Main
Ни каких библиотек в jar
### KotlinMin
Только `kotlin-stdlib`. Версия котлин: 2.0.21
### KotlinMax 
`kotlin-stdlib` и `kotlin-reflect`. Версия котлин: 2.0.21
EN:
File format: <br>
`PikoPluginLib-Group-MinecraftVersion-PluginVersion.jar`<br>
## Groups
Groups are created to provide variety of jar options and prevent errors related to identical libraries in different plugins, for example, two different jars with Kotlin and without

### Main
No libraries in jar
### KotlinMin
Only `kotlin-stdlib`. Kotlin version: 2.0.21
### KotlinMax 
`kotlin-stdlib` and `kotlin-reflect`. Kotlin version: 2.0.21
# Установка | Install
```groovy
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    compileOnly 'com.github.PIKO8:PikoPluginLib:v0.1.13'
}
```
или | or
[![](https://jitpack.io/v/PIKO8/PikoPluginLib.svg)](https://jitpack.io/#PIKO8/PikoPluginLib)<br>
<br>
И добавить в `plugin.yml`<br>
And add to `plugin.yml`<br>
```yml
depend: [PikoPluginLib]
```
# Вики | Wiki
Wiki only in Russian<br>
https://github.com/PIKO8/PikoPluginLib/wiki
