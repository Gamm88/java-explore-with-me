package ru.practicum.event.model.event;

import ru.practicum.exeptions.ValidatorExceptions;

public enum State {
    // на рассмотрении
    PENDING,
    // опубликовано
    PUBLISHED,
    // отменено
    CANCELED;

    private static final State[] copyOfValues = values();

    public static State getStateOrValidatorExceptions(String state) {
        for (State enumState : copyOfValues) {
            if (enumState.name().equals(state)) {
                return enumState;
            }
        }
        throw new ValidatorExceptions("Неизвестный статус: " + state);
    }
}