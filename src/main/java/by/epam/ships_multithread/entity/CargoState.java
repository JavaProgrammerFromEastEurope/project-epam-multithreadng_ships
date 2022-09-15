package by.epam.ships_multithread.entity;

import java.util.Random;

import static java.lang.System.out;

public class CargoState extends State {
    Random rand = new Random();
    int cargo;

    public CargoState(StatePool pool) {
        super(pool);
    }

    protected void performService() {
        cargo = rand.nextInt(120);
        assembler.ship().addCargo(cargo);
        out.println(this + " adding cargo " + cargo);
    }
}
