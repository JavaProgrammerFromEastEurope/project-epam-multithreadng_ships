package by.epam.ships_multithread.entity;

import java.util.Random;

import static java.lang.System.out;

public class CargoOrderState extends State {
    Random rand = new Random();
    int cargo;

    public CargoOrderState(StatePool pool) {
        super(pool);
    }

    protected void performService() {
        cargo = rand.nextInt(120);
        assembler.ship().addCargoOrder(cargo);
        out.println(this + " adding cargoOrder " + cargo);
    }
}
