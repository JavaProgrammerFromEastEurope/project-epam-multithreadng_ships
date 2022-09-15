package by.epam.ships_multithread.command.impl

import by.epam.network.Message
import by.epam.network.Message.Companion.makeObjectFromMessage
import by.epam.ships_multithread.application.util.Const.logger
import by.epam.ships_multithread.command.Command
import by.epam.ships_multithread.entity.Ship
import org.apache.logging.log4j.Level
import java.lang.System.out

class TakeShipCommand : Command() {

    override fun execute(message: Message?) {
        out.printf("%s - action completed successfully%n", message!!.name)
        val ships = makeObjectFromMessage(message) as List<Ship>?
        logger.printf(
            Level.INFO,
            "The Ship %s was taken with cargo %s",
                ships!![ships.size-1].name, ships[ships.size-1].cargo
        )
    }
}