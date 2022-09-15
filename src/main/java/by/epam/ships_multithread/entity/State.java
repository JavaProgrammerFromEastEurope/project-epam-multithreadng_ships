package by.epam.ships_multithread.entity;

import org.apache.logging.log4j.Level;

import java.util.concurrent.BrokenBarrierException;

import static by.epam.ships_multithread.application.util.Const.logger;

public abstract class State implements Runnable {
    private final StatePool pool;
    private boolean started = false;
    protected Assembler assembler;

    public State(StatePool sPool) {
        pool = sPool;
    }

    public void assignAssembler(Assembler assembler) {
        this.assembler = assembler;
    }

    public synchronized void engage() {
        started = true;
        notifyAll();
    }

    abstract protected void performService();

    public void run() {
        try {
            powerDown();
            while (!Thread.interrupted()) {
                performService();
                assembler.barrier().await();
                powerDown();
            }
        } catch (InterruptedException e) {
            logger.printf(Level.ERROR, this + " was interrupted");
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized void powerDown() throws InterruptedException {
        started = false;
        assembler = null;
        pool.release(this);
        while (!started) wait();
    }

    public String toString() {
        return getClass().getName();
    }
}
