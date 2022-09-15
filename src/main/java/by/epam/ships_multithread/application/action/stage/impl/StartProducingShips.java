package by.epam.ships_multithread.application.action.stage.impl;

import by.epam.ships_multithread.application.action.Action;
import by.epam.ships_multithread.application.action.BaseStageAction;

public class StartProducingShips extends BaseStageAction implements Action {

    public String description() {
        return "Start Producing Ships\n";
    }

    public void action() {
        serviceActions.startProduceObjectAction();
    }

}
