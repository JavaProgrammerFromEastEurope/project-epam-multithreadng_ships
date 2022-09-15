package by.epam.ships_multithread.application.util;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.*;

class Ship {
    private final long id;
    private Phonetic name;
    private int cargo, cargoOrder;
    private boolean ready;

    public Ship(long id) {
        this.id = id;
    }

    public Ship() {
        id = -1;
    }

    public synchronized long getId() {
        return id;
    }

    public synchronized void addName(Phonetic name) {
        this.name = name;
    }

    public synchronized void addCargo(int cargo) {
        this.cargo = cargo;
    }

    public synchronized void addCargoOrder(int cargoOrder) {
        this.cargoOrder = cargoOrder;
    }

    public synchronized void makeReady() {
        this.ready = true;
    }

    @Override
    public synchronized String toString() {
        return "Ship " + id + " ["
                + " name: " + name
                + " cargo: " + cargo
                + " cargo order: " + cargoOrder
                + " ship ready: " + ready + " ]";
    }
}

class ShipQueue extends LinkedBlockingQueue<Ship> {}

class ShipPreparer implements Runnable {
    private ShipQueue shipQueue;
    private int counter = 0;

    public ShipPreparer(ShipQueue shipQueue) {
        this.shipQueue = shipQueue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                TimeUnit.MILLISECONDS.sleep(1000);
                System.out.println("---------------------------------------------");
                // Make preparations:
                Ship ship = new Ship(counter++);
                System.out.println("Ship preparation: " + ship);
                // Insert into queue
                shipQueue.put(ship);
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted: ShipPreparation");
        }
        System.out.println("ShipPreparation off");
    }
}

class Finnisher implements Runnable {
    private ShipQueue shipQueue, finishQueue;
    private Ship ship;
    private CyclicBarrier barrier = new CyclicBarrier(5);
    private StatePool statePool;

    public Finnisher(ShipQueue sq, ShipQueue fq, StatePool sp) {
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
                // Blocks until chassis is available:
                ship = shipQueue.take();
                // Hire robots to perform work:
                statePool.hire(NameState.class, this);
                statePool.hire(CargoState.class, this);
                statePool.hire(CargoOrderState.class, this);
                statePool.hire(ShipReadyState.class, this);
                barrier.await(); // Until the states finish
                // Put ship into finishingQueue for further work
                finishQueue.put(ship);
            }
        } catch (InterruptedException e) {
            System.out.println("Exiting Assembler via interrupt");
        } catch (BrokenBarrierException e) {
            // This one we want to know about
            throw new RuntimeException(e);
        }
        System.out.println("Assembler off");
    }
}

class Reporter implements Runnable {
    private ShipQueue shipQueue;

    public Reporter(ShipQueue sq) {
        shipQueue = sq;
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                System.out.println(shipQueue.take());
            }
        } catch (InterruptedException e) {
            System.out.println("Exiting Reporter via interrupt");
        }
        System.out.println("Reporter off");
    }
}

abstract class State implements Runnable {
    private StatePool pool;
    private boolean started = false;
    protected Finnisher assembler;

    public State(StatePool sPool) {
        pool = sPool;
    }

    public State assignAssembler(Finnisher assembler) {
        this.assembler = assembler;
        return this;
    }

    public synchronized void engage() {
        started = true;
        notifyAll();
    }

    abstract protected void performService();

    public void run() {
        try {
            powerDown(); // Wait until needed
            while (!Thread.interrupted()) {
                performService();
                assembler.barrier().await(); // Synchronize
                // We're done with that job...
                powerDown();
            }
        } catch (InterruptedException e) {
            System.out.println("Exiting " + this + " via interrupt");
        } catch (BrokenBarrierException e) {
            // This one we want to know about
            throw new RuntimeException(e);
        }
        System.out.println(this + " off");
    }

    private synchronized void powerDown() throws InterruptedException {
        started = false;
        assembler = null; // Disconnect from the Assembler
        // Put ourselves back in the available pool:
        pool.release(this);
        while (started == false) // Power down
            wait();
    }

    public String toString() {
        return getClass().getName();
    }
}

class NameState extends State {
    Phonetics[] phonetics;
    Phonetic phonetic;

    public NameState(StatePool pool) {
        super(pool);
        phonetics = Phonetics.values();
    }

    protected void performService() {
        phonetic = phonetics[0].randomSelection();
        System.out.println(this + " adding name " + phonetic.toString());
        assembler.ship().addName(phonetic);
    }
}

class CargoState extends State {
    Random rand = new Random();
    int cargo;

    public CargoState(StatePool pool) {
        super(pool);
    }

    protected void performService() {
        cargo = rand.nextInt(120);
        assembler.ship().addCargo(cargo);
        System.out.println(this + " adding cargo " + cargo);
    }
}

class CargoOrderState extends State {
    Random rand = new Random();
    int cargo;

    public CargoOrderState(StatePool pool) {
        super(pool);
    }

    protected void performService() {
        cargo = rand.nextInt(120);
        assembler.ship().addCargoOrder(cargo);
        System.out.println(this + " adding cargoOrder " + cargo);
    }
}

class ShipReadyState extends State {
    public ShipReadyState(StatePool pool) {
        super(pool);
    }

    protected void performService() {
        System.out.println(this + " make ready");
        assembler.ship().makeReady();
    }
}

class StatePool {
    // Quietly prevents identical entries:
    private Set<State> pool = new HashSet<State>();

    public synchronized void add(State state) {
        pool.add(state);
        notifyAll();
    }

    public synchronized void hire(Class<? extends State> stateType, Finnisher assembler)
            throws InterruptedException {
        for (State state : pool)
            if (state.getClass().equals(stateType)) {
                pool.remove(state);
                state.assignAssembler(assembler);
                state.engage(); // Power it up to do the task
                return;
            }
        wait(); // None available
        hire(stateType, assembler); // Try again, recursively
    }

    public synchronized void release(State state) {
        add(state);
    }
}

public class ShipBuilder {
    public static void main(String[] args) throws Exception {
        ShipQueue shipQueue = new ShipQueue(),
                finishingQueue = new ShipQueue();
        ExecutorService exec = Executors.newFixedThreadPool(8);
        StatePool statePool = new StatePool();
        exec.execute(new NameState(statePool));
        exec.execute(new CargoState(statePool));
        exec.execute(new CargoOrderState(statePool));
        exec.execute(new ShipReadyState(statePool));
        exec.execute(new Finnisher(
                shipQueue, finishingQueue, statePool));
        exec.execute(new Reporter(finishingQueue));
        // Start everything running by producing chassis:
        exec.execute(new ShipPreparer(shipQueue));
        TimeUnit.SECONDS.sleep(20);
        exec.shutdownNow();
    }
}