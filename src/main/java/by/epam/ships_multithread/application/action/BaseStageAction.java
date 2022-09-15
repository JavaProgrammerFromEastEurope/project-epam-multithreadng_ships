package by.epam.ships_multithread.application.action;

import by.epam.ships_multithread.controller.ActionController;
import by.epam.ships_multithread.controller.impl.ShipController;

public abstract class BaseStageAction {
    public final ActionController serviceActions;

    public BaseStageAction() {
        this.serviceActions = new ShipController();
    }
}
