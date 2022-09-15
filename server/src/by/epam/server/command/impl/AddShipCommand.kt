package by.epam.server.command.impl

import by.epam.network.Message
import by.epam.network.Message.Companion.makeMessageWithBytes
import by.epam.network.Message.Companion.makeObjectFromMessage
import by.epam.server.command.Command
import by.epam.ships_multithread.entity.Ship

class AddShipCommand : Command() {
    override fun execute(message: Message?): Message? {
        try {
            val ship = makeObjectFromMessage(message!!) as Ship?
            serialShips.add(ship!!)
            return makeMessageWithBytes("takeShip", serialShips.get())
        } catch (e: Exception) {
            makeMessageWithBytes(
                "exception", String.format("Exception while adding ships: %s", e.message)
            )
        }
        throw NullPointerException()
    }
}