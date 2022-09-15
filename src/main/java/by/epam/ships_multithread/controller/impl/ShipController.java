package by.epam.ships_multithread.controller.impl;

import by.epam.ships_multithread.controller.ActionController;
import by.epam.ships_multithread.entityAction.ActionFactory;
import by.epam.ships_multithread.entityAction.ActionTemplate;

public class ShipController implements ActionController {

    private final ActionTemplate shipsActions;

    public ShipController() {
        this.shipsActions = ActionFactory.getInstance().getShipActions();
    }

    @Override
    public void startProduceObjectAction() {
        shipsActions.startProduceObjectAction();
    }
}
