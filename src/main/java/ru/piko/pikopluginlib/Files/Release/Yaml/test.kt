package ru.piko.pikopluginlib.Files.Release.Yaml

import ru.piko.pikopluginlib.Files.Abstract.folder.AbstractFolder
import ru.piko.pikopluginlib.Files.Abstract.folder.AbstractParentFolder
import ru.piko.pikopluginlib.Files.Abstract.folder.AbstractPikoPluginFolder
import ru.piko.pikopluginlib.Main

/*
	Информация:
	Этот файл сделан, что бы я видел итоговую структуру и стремился к ней поэтому это тестовый файл `test.kt`.
	Я сначала пишу тут как я хотел что бы оно выглядело, а потом пытаюсь к этому прийти.
	Не знаю на сколько это удачная стратегия, но как будет так будет если что переделаю
 */

val plugin = Main.getPlugin() ?: error("")

class MyPluginFolder : AbstractPikoPluginFolder<Main>(plugin) {

//	val itemsFolder: ItemsFolder = ItemsFolder(this)
//
//	val configFile: ConfigFile = ConfigFile(this)

}

//class ItemsFolder(parent: AbstractFolder) : AbstractParentFolder("items", parent) {
//
//	val items: ItemsFile = ItemsFile(this)
//
//}

//class ItemsFile(parent: AbstractFolder) : YamlMapFile<YamlItemObject>("items", parent) {
//
//}

//class YamlItemObject : YamlObjectValue

//class ConfigFile(parent: AbstractFolder) : YamlFile("config", parent) {
//
//	val version = YamlKeyValue<String>(this, "version", "1")
//}