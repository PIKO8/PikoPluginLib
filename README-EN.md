# PikoPluginLib

Current minecraft version: `1.21.7`
UPD: In fact, the project is half-abandoned and will probably be completely abandoned soon.

**My Minecraft Plugin Development Library**  
Initial version was inspired by [Kodi Simpson's YouTube channel](https://www.youtube.com/@KodySimpson) 
([Spigot playlist](https://www.youtube.com/playlist?list=PLfu_Bpi_zcDNEKmR82hnbv9UxQ16nUBF7)), 
but has been completely rewritten and extended.

🔍 **Important**: 
- Current architecture differs significantly from original videos
- Only some core concepts were preserved
- Most code was written from scratch

## Installation
```kotlin
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.PIKO8:PikoPluginLib:1.21.7-0.1.19")
}
```

Add to `plugin.yml`:
```yml
depend: [PikoPluginLib]
```

[Русская версия](README-RU.md) | [Wiki (Only Russian)](https://github.com/PIKO8/PikoPluginLib/wiki)
