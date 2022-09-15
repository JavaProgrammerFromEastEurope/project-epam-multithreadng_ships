package by.epam.ships_multithread.application.exception

class StopApplicationException : Exception() {
    override val message: String
        get() = "The app has finished it's work! (unprintable)"
}