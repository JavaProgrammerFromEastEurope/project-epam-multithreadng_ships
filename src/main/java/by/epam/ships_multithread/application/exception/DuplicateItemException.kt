package by.epam.ships_multithread.application.exception

class DuplicateItemException(private val id: Long) : Exception() {
    override val message: String
        get() = String.format("Duplicate item with id = %d", id)
}