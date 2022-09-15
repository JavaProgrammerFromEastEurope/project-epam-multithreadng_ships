package by.epam.server.command

import by.epam.network.Message
import by.epam.server.dao.serializer.SerializableShip

abstract class Command {
    protected val serialShips = SerializableShip()
    abstract fun execute(message: Message?): Message?
}