package by.epam.server.dao.serializer

import by.epam.server.dao.ShipService
import by.epam.ships_multithread.entity.Ship
import java.io.*
import java.util.*

class SerializableShip : ShipService {
    override fun get(): MutableList<Ship> {
        try {
            javaClass.getResourceAsStream(String.format("/%s", PATH)).use { stream ->
                val ios = ObjectInputStream(stream)
                return ios.readObject() as MutableList<Ship>
            }
        } catch (e: IOException) {
            return ArrayList()
        } catch (e: ClassNotFoundException) {
            return ArrayList()
        }
    }

    override fun save(students: List<Ship>) {
        try {
            val classLoader = javaClass.classLoader
            val file = File(Objects.requireNonNull(classLoader.getResource(PATH)).file)
            val oos = ObjectOutputStream(FileOutputStream(file))
            oos.writeObject(students)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val PATH = "by/epam/server/resources/orders.bin"
    }
}