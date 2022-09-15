package by.epam.ships_multithread.entity;

import java.util.HashSet;
import java.util.Set;

public class StatePool {
    private final Set<State> pool = new HashSet<>();

    public synchronized void add(State state) {
        pool.add(state);
        notifyAll();
    }

    public synchronized void hire(Class<? extends State> stateType, Assembler assembler)
            throws InterruptedException {
        for (State state : pool)
            if (state.getClass().equals(stateType)) {
                pool.remove(state);
                state.assignAssembler(assembler);
                state.engage();
                return;
            }
        wait();
        hire(stateType, assembler);
    }

    public synchronized void release(State state) {
        add(state);
    }
}
