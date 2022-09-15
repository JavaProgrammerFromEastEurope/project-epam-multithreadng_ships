package by.epam.ships_multithread.entity

import by.epam.ships_multithread.application.util.Phonetic
import java.io.Serializable

class Ship(@get:Synchronized var id: Long) : Serializable {
    var name: Phonetic? = null
        private set
    var cargo = 0
        private set
    var cargoOrder = 0
        private set
    var isReady = false
        private set

    @Synchronized
    fun addName(name: Phonetic?) {
        this.name = name
    }

    @Synchronized
    fun addCargo(cargo: Int) {
        this.cargo = cargo
    }

    @Synchronized
    fun addCargoOrder(cargoOrder: Int) {
        this.cargoOrder = cargoOrder
    }

    @Synchronized
    fun makeReady() {
        isReady = true
    }

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            javaClass != other?.javaClass -> false
            else -> {
                other as Ship
                when {
                    id != other.id -> false
                    name != other.name -> false
                    cargo != other.cargo -> false
                    cargoOrder != other.cargoOrder -> false
                    isReady != other.isReady -> false
                    else -> true
                }
            }
        }
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + cargo
        result = 31 * result + cargoOrder
        result = 31 * result + isReady.hashCode()
        return result
    }

    override fun toString(): String {
        return "Ship [ id=$id, name=$name, cargo=$cargo, " +
                "cargoOrder=$cargoOrder, isReady=$isReady ]"
    }
}