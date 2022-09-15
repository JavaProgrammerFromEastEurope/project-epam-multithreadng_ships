//: enumerated/menu/Course.java
package by.epam.ships_multithread.application.util;

public enum Phonetics {
    PHONETIC(Phonetic.Phonetics.class);

    private final Phonetic[] values;

    Phonetics(Class<? extends Phonetic> kind) {
        values = kind.getEnumConstants();
    }

    public Phonetic randomSelection() {
        return Enums.random(values);
    }
} /// :~
