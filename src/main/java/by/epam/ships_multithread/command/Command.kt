package by.epam.ships_multithread.command

import by.epam.network.Message


abstract class Command {
    abstract fun execute(message: Message?)
}