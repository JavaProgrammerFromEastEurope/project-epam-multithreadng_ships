package by.epam.ships_multithread.application.exception

class WrongValueException(private val value: Int) : Exception() {
    override val message: String
        get() {
            val character = 122891
            return String.format("Don't be a stranger, world is beautiful %c  !" +
                    " %d - is Wrong!", character.toChar(), value)
        }
}