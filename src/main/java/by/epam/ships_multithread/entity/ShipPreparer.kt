package by.epam.ships_multithread.entity

import by.epam.ships_multithread.application.util.Const
import org.apache.logging.log4j.Level
import java.util.concurrent.TimeUnit

class ShipPreparer(
    private val shipQueue: ShipQueue) : Runnable {
    private var counter = 0
    override fun run() {
        try {
            while (!Thread.interrupted()) {
                TimeUnit.MILLISECONDS.sleep(2000)
                val ship = Ship(counter++.toLong())
                println("Ship preparation: $ship")
                shipQueue.put(ship)
            }
        } catch (e: InterruptedException) {
            Const.logger.printf(Level.ERROR, this.toString() + "was interrupted")
        }
    }
}