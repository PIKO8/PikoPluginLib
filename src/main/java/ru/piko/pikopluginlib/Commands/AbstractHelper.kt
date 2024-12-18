package ru.piko.pikopluginlib.Commands

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
abstract class AbstractHelper : AbstractCommand()