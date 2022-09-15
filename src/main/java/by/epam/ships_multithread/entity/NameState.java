package by.epam.ships_multithread.entity;

import by.epam.ships_multithread.application.util.Phonetic;
import by.epam.ships_multithread.application.util.Phonetics;

import static java.lang.System.out;

public class NameState extends State {
    Phonetics[] phonetics;
    Phonetic phonetic;

    public NameState(StatePool pool) {
        super(pool);
        phonetics = Phonetics.values();
    }

    protected void performService() {
        phonetic = phonetics[0].randomSelection();
        out.println(this + " adding name " + phonetic.toString());
        assembler.ship().addName(phonetic);
    }
}
