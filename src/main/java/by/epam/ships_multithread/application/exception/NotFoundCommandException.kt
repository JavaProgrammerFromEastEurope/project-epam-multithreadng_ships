package by.epam.ships_multithread.application.exception

class NotFoundCommandException(private val commandName: String) : Exception() {
    override val message: String
        get() = String.format("Not found command with name = %s", commandName)
}