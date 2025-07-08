# PikoPluginLib

Текущая версия майнкрафт: `1.21.7`
UPD: На самом деле проект полузаброшеный в скором времени вероятно всего будет полностью заброшен.

**Моя библиотека для разработки Minecraft-плагинов**  
Изначальная версия основана на материалах YouTube-канала [Kodi Simpson](https://www.youtube.com/@KodySimpson) 
([плейлист по Spigot](https://www.youtube.com/playlist?list=PLfu_Bpi_zcDNEKmR82hnbv9UxQ16nUBF7)), 
но была кардинально переработана и дополнена. 

🔍 **Важно**: 
- Текущая архитектура и реализация значительно отличаются от показанной в видео
- Сохранены только отдельные концепции из оригинальных уроков
- Бóльшая часть кода написана с нуля

## Установка
```kotlin
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.PIKO8:PikoPluginLib:1.21.7-0.1.19")
}
```

Добавить в `plugin.yml`:
```yml
depend: [PikoPluginLib]
```

[English Version](README-EN.md) | [Вики](https://github.com/PIKO8/PikoPluginLib/wiki)
