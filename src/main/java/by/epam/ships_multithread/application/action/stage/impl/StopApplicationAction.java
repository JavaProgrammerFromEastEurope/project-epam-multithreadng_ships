package by.epam.ships_multithread.application.action.stage.impl;

import by.epam.ships_multithread.application.action.Action;
import by.epam.ships_multithread.application.exception.StopApplicationException;

public class StopApplicationAction implements Action{

    public String description() {
        return "Exit From App\n";
    }

    public void action() throws StopApplicationException {
        throw new StopApplicationException();
    }
}
