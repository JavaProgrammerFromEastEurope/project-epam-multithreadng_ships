package by.epam.server.command

import by.epam.server.command.impl.AddShipCommand
import by.epam.ships_multithread.application.exception.NotFoundCommandException

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
        commands["sendShip"] = AddShipCommand()
    }
}