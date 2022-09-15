package by.epam.ships_multithread.receiver

import by.epam.network.CommunicationBridge
import by.epam.network.Message
import by.epam.network.TCPConnectionListener
import by.epam.ships_multithread.application.exception.NotFoundCommandException
import by.epam.ships_multithread.application.util.Const.logger
import by.epam.ships_multithread.command.CommandFactory
import org.apache.logging.log4j.Level


class Receiver(
    private val ipAddress: String?,
    private val port: Int,
) : TCPConnectionListener {
    fun run(): Boolean {
        return try {
            communicationBridge = CommunicationBridge(this, ipAddress, port)
            true
        } catch (e: Exception) {
            false
        }
    }
    private var connection = Connection()
    override fun onConnectionReady(tcpConnection: CommunicationBridge?) {
        println("Connection Established")
    }

    override fun onReceiveString(tcpConnection: CommunicationBridge?, value: String?) {
        println(value)
    }

    override fun onReceiveMessage(tcpConnection: CommunicationBridge?, message: Message?) {
        asyncReadMessages(message)
        connection.connect()
    }

    @Synchronized
    private fun asyncReadMessages(message: Message?) {
        if (message!!.name == "exception") {
            logger.printf(Level.ERROR, "return exception:\n %s", listOf(message.data))
        } else {
            try {
                CommandFactory.instance.getCommand(message.name)!!.execute(message)
            } catch (e: NotFoundCommandException) {
                e.printStackTrace()
            }
        }
    }

    override fun onDisconnect(tcpConnection: CommunicationBridge?) {
        println("Connection Close")
    }

    override fun onException(tcpConnection: CommunicationBridge?, e: Exception?) {
        logger.printf(Level.ERROR, "$this connection was broken %n")
    }

    companion object {
        var communicationBridge: CommunicationBridge? = null
    }
}