package by.epam.ships_multithread.entityAction.impl;

import by.epam.ships_multithread.entity.*;
import by.epam.ships_multithread.entityAction.ActionTemplate;
import org.apache.logging.log4j.Level;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static by.epam.ships_multithread.application.util.Const.logger;

public class ShipActions implements ActionTemplate {

    @Override
    public void startProduceObjectAction() {
        ShipQueue shipQueue = new ShipQueue(),
                finishingQueue = new ShipQueue();
        ExecutorService exec = Executors.newFixedThreadPool(7);
        StatePool statePool = new StatePool();
        exec.execute(new NameState(statePool));
        exec.execute(new CargoState(statePool));
        exec.execute(new CargoOrderState(statePool));
        exec.execute(new ShipReadyState(statePool));
        exec.execute(new Assembler(
                shipQueue, finishingQueue, statePool));
        exec.execute(new Finalizer(finishingQueue));
        // Start everything running by producing ships:
        exec.execute(new ShipPreparer(shipQueue));
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            logger.printf(Level.ERROR, this + "was interrupted");
        }
        exec.shutdownNow();
    }
}
