package by.epam.ships_multithread.entity;

import org.apache.logging.log4j.Level;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import static by.epam.ships_multithread.application.util.Const.logger;

public class Assembler implements Runnable {
    private final ShipQueue shipQueue;
    private final ShipQueue finishQueue;
    private Ship ship;
    private final CyclicBarrier barrier = new CyclicBarrier(5);
    private final StatePool statePool;

    public Assembler(ShipQueue sq, ShipQueue fq, StatePool sp) {
        shipQueue = sq;
        finishQueue = fq;
        statePool = sp;
    }

    public Ship ship() {
        return ship;
    }

    public CyclicBarrier barrier() {
        return barrier;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                ship = shipQueue.take();
                statePool.hire(NameState.class, this);
                statePool.hire(CargoState.class, this);
                statePool.hire(CargoOrderState.class, this);
                statePool.hire(ShipReadyState.class, this);
                barrier.await();
                finishQueue.put(ship);
            }
        } catch (InterruptedException e) {
            logger.printf(Level.ERROR, this + " was interrupted");
        } catch (BrokenBarrierException e) {
            logger.printf(Level.ERROR, this + " barrier was broken");
        }
    }
}
