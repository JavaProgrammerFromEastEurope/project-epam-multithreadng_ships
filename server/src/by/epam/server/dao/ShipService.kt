package by.epam.server.dao

import by.epam.ships_multithread.application.exception.NotFoundItemException
import by.epam.ships_multithread.entity.Ship

interface ShipService {
    fun get(): MutableList<Ship>

    fun add(newShip: Ship) {
        val ships = get()
        ships
            .filter { it.id == newShip.id }
            .forEach { _ ->
                (ships[ships.size - 1].id + 1)
                    .also { newShip.id = it }
            }
        ships.add(newShip)
        save(ships)
    }

    @Throws(NotFoundItemException::class)
    fun update(newShip: Ship) {
        val ships = get()
        ships.withIndex().forEach { (index, ship) ->
            when (ship.id) {
                newShip.id -> {
                    ships[index] = newShip
                    save(ships)
                    return
                }
            }
        }
    }

    @Throws(NotFoundItemException::class)
    fun remove(removeID: Long) {
        val ships = get()
        ships.forEach { ship ->
            when (ship.id) {
                removeID -> {
                    ships.remove(ship)
                    save(ships)
                    return
                }
            }
        }
    }

    fun save(students: List<Ship>)
}