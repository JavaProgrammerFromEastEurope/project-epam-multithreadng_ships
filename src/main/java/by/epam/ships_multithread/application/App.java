package by.epam.ships_multithread.application;

import by.epam.ships_multithread.application.action.Action;
import by.epam.ships_multithread.application.action.StageActionFactory;
import by.epam.ships_multithread.application.exception.StopApplicationException;
import by.epam.ships_multithread.application.exception.WrongValueException;
import by.epam.ships_multithread.application.util.Input;
import by.epam.ships_multithread.receiver.Connection;

import static java.lang.System.out;

public final class App {

    private static final StageActionFactory stageFactory = StageActionFactory.getInstance();

    public static void startEntertainment() {
        out.println("Welcome! Entertainment has become!");
        actionStages();
    }

    private static void actionStages() {
        while (true) try {
            getActionStage().action();
        } catch (StopApplicationException e) {
            break;
        }
    }

    private static Action getActionStage() {
        stageFactory.showStageMenu();
        int index = Input.anyIntInitialize();
        try {
            return stageFactory.getActionStage(index);
        } catch (WrongValueException e) {
            out.println(e.getMessage());
        }
        return getActionStage();
    }

    public static void main(String[] args) {
        Connection connection = new Connection();
        connection.connect();
        App.startEntertainment();
    }
}
