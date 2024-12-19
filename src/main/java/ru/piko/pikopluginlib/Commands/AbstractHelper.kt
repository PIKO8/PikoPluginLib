package ru.piko.pikopluginlib.Commands

import org.bukkit.command.CommandSender

/**
 * RU:
 *
 * Абстрактный обработчик в [CommandManager] добавляет help.
 *
 * Работает только с подкомандами типа [HelperSubCommand]
 *
 * Требуется что бы добавлять команду help
 *
 */
abstract class AbstractHelper : AbstractCommand() {
	
	abstract fun page(sender: CommandSender, number: Int)
	
	abstract fun command(sender: CommandSender, command: String)
}