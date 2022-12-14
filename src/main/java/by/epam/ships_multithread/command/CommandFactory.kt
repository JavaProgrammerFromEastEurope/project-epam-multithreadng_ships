package by.epam.ships_multithread.command

import by.epam.ships_multithread.application.exception.NotFoundCommandException
import by.epam.ships_multithread.command.impl.TakeShipCommand

class CommandFactory private constructor() {
    private val commands = HashMap<String, Command>()

    @Throws(NotFoundCommandException::class)
    fun getCommand(commandName: String): Command? {
        if (commands.containsKey(commandName)) {
            return commands[commandName]
        }
        throw NotFoundCommandException(commandName)
    }

    companion object {
        val instance = CommandFactory()
    }

    init {
        commands["takeShip"] = TakeShipCommand()
    }
}