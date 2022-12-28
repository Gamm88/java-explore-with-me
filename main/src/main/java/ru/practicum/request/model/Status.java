package ru.practicum.request.model;

import ru.practicum.exeptions.ValidatorExceptions;

public enum Status {
    // на рассмотрении
    PENDING,
    // подтвержден
    CONFIRMED,
    // отклонён
    REJECTED,
    // отменён
    CANCELED;

    private static final Status[] copyOfValues = values();

    public static Status getStatusOrValidatorExceptions(String status) {
        for (Status enumStatus : copyOfValues) {
            if (enumStatus.name().equals(status)) {
                return enumStatus;
            }
        }
        throw new ValidatorExceptions("Неизвестный статус: " + status);
    }
}