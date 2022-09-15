package by.epam.ships_multithread.entity;

import static java.lang.System.out;

public class ShipReadyState extends State {
    public ShipReadyState(StatePool pool) {
        super(pool);
    }

    protected void performService() {
        out.println(this + " make ready");
        assembler.ship().makeReady();
    }
}
