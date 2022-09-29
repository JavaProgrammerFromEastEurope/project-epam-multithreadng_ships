package by.epam.ships_multithread.entityAction;

import by.epam.ships_multithread.entityAction.impl.ShipActions;

import java.util.HashMap;
import java.util.Map;

public class ActionFactory {

    private static final ActionFactory instance = new ActionFactory();

    private final Map<Integer, ActionTemplate> shipActions = new HashMap<>();

    public static ActionFactory getInstance() {
        return instance;
    }

    private ActionFactory() {
        shipActions.put(1, new ShipActions());
    }

    public ActionTemplate getShipActions() {
        return shipActions.get(1);
    }
}
