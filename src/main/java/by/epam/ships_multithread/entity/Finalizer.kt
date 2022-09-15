package by.epam.ships_multithread.entity

import by.epam.network.Message.Companion.makeMessageWithBytes
import by.epam.ships_multithread.application.util.Const.logger
import by.epam.ships_multithread.receiver.Receiver
import org.apache.logging.log4j.Level

class Finalizer(private val shipQueue: ShipQueue) : Runnable {
    override fun run() {
        try {
            while (!Thread.interrupted()) {
                val ship = shipQueue.take()
                println(ship)
                println("---------------------------------------------")
                logger.printf(Level.ERROR, "$ship ")
                Receiver.communicationBridge!!
                    .sendMessage(makeMessageWithBytes("sendShip", ship))
            }
        } catch (e: InterruptedException) {
            logger.printf(Level.ERROR, "$this was interrupted")
        }
    }
}