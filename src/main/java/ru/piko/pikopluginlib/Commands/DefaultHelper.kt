package ru.piko.pikopluginlib.Commands

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.CommandSender
import ru.piko.pikopluginlib.Extends.ComponentExtend.mini
import ru.piko.pikopluginlib.Utils.UText.color

class DefaultHelper(private val commandsPerPage: Int = 5) : AbstractHelper() {
	override val name: String = "help"
	
	override fun perform(sender: CommandSender, args: Array<String>) {
		val commands = commandManager.commands
			.filterIsInstance<HelperSubCommand>()
			.filter { it.hasPermission(sender, args) }
		
		when {
			// Вывод информации о конкретной команде
			args.size >= 3 && args[1] == "command" -> {
				val commandName = args[2]
				val command = commands.find { it.name.equals(commandName, ignoreCase = true) }
				
				if (command != null) {
					sender.sendMessage(color("&7========================================"))
					sender.sendMessage(color("&6Информация о команде: &a${command.name}"))
					sender.sendMessage(color("&7========================================"))
					sender.sendMessage(color("&6Описание: &a${command.description}"))
					sender.sendMessage(color("&6Синтаксис: &a${command.syntax}"))
					sender.sendMessage(color("&7========================================"))
				} else {
					sender.sendMessage(color("&cКоманда &6${commandName}&c не найдена."))
				}
			}
			
			// Постраничный вывод всех команд (существующая логика)
			else -> {
				val page = if (args.size >= 3 && args[1] == "page") {
					args[2].toIntOrNull() ?: 1
				} else 1
				
				val totalPages = (commands.size + commandsPerPage - 1) / commandsPerPage
				val pageCommands = commands
					.drop((page - 1) * commandsPerPage)
					.take(commandsPerPage)
				
				sender.sendMessage(color("&7========================================"))
				sender.sendMessage(color("&6Список команд &7(Страница $page/$totalPages)"))
				sender.sendMessage(color("&7========================================"))
				
				for (com in pageCommands) {
					sender.sendMessage(mini("""
					    <gold><click:run_command='/help command ${com.name}'><hover:show_text:'<gray>Нажмите для подробной информации о команде'>${com.name}</hover></click></gold>
					    <white>- </white>
					    <green>${com.description}</green>
					"""))
				}
				
				sendPageNavigation(sender, page, totalPages)
			}
		}
	}
	
	private fun sendPageNavigation(sender: CommandSender, currentPage: Int, totalPages: Int) {
		val mm = MiniMessage.miniMessage()
		val navigationMessage = mm.deserialize("""
        <gray>===== <red><click:run_command='/help page ${maxOf(1, currentPage - 1)}'><hover:show_text:'<gray>Предыдущая страница'><<</hover></click></red>
        <gray>==
        <green>$currentPage</green>/<green>$totalPages</green>
        <gray>==
        <red><click:run_command='/help page ${minOf(totalPages, currentPage + 1)}'><hover:show_text:'<gray>Следующая страница'>>></hover></click></red>
        =====
    """.trimIndent())
		
		sender.sendMessage(navigationMessage)
	}
	
	override fun arguments(sender: CommandSender, args: Array<String>): List<String> {
		val commands = commandManager.commands
			.filterIsInstance<HelperSubCommand>()
			.filter { it.hasPermission(sender, args) }
		val totalPages = (commands.size + commandsPerPage - 1) / commandsPerPage
		
		return when (args.size) {
			2 -> listOf("command", "page")
			3 -> when (args.getOrNull(1)) {
				null -> listOf("")
				"command" -> commands.map { it.name }
				"page" -> (1..totalPages).map { it.toString() }
				else -> listOf("")
			}
			else -> listOf("")
		}
	}
}