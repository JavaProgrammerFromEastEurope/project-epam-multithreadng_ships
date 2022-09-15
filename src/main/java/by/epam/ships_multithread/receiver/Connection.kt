package by.epam.ships_multithread.receiver

import by.epam.ships_multithread.application.util.Const.logger
import org.apache.logging.log4j.Level
import java.io.IOException
import java.lang.System.out
import java.util.*

class Connection() {
    private var ipAddress: String? = null
    private var port = 0

    fun connect() {
        /** Do some preparation work
         * and start the receiver */
        readPropertyFromFile()
        startReceiver()
    }

    private fun readPropertyFromFile() = try {
        val property = Properties().also {
            it.load(Connection::class.java.getResourceAsStream(String.format("/%s", PATH)))
            ipAddress = it.getProperty("ipAddress")
            port = it.getProperty("port").toInt()
        }
    } catch (e: IOException) {
        println(e.message)
    }

    private fun startReceiver() {
        var trying = 0
        while (!Receiver(ipAddress, port).run()) {
            out.printf("%s try connect to server is failed!%n", ++trying)
            try {
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                logger.printf(Level.ERROR, "$this was interrupted")
            }
        }
    }

    companion object {
        private const val PATH = "config/receiver.properties"
    }
}