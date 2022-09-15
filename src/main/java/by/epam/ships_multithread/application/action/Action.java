package by.epam.ships_multithread.application.action;

import by.epam.ships_multithread.application.exception.StopApplicationException;

public interface Action {
    String description();

    void action() throws StopApplicationException;
}
