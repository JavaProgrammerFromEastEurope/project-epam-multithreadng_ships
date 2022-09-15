package by.epam.ships_multithread.application.action;

import by.epam.ships_multithread.application.action.stage.impl.StartProducingShips;
import by.epam.ships_multithread.application.action.stage.impl.StopApplicationAction;
import by.epam.ships_multithread.application.exception.WrongValueException;

import java.util.HashMap;
import java.util.Map;

import static java.lang.System.out;

public class StageActionFactory {

    private static final StageActionFactory instance = new StageActionFactory();

    private final Map<Integer, Action> defaultStage = new HashMap<>();

    public static StageActionFactory getInstance() {
        return instance;
    }

    private StageActionFactory() {
        defaultStage.put(0, new StopApplicationAction());
        defaultStage.put(1, new StartProducingShips());
    }

    public Action getActionStage(int index) throws WrongValueException {
        if (defaultStage.containsKey(index)) {
            return defaultStage.get(index);
        }
        throw new WrongValueException(index);
    }

    public void showStageMenu() {
        defaultStage.forEach((key, value) ->
            out.printf("%d - %s%n", key, value.description()));
        out.println("Choose the action you want:");
    }
}
