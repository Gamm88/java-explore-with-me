package ru.practicum.components;

import org.springframework.stereotype.Component;
import ru.practicum.exeptions.ValidatorExceptions;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class DateUtility {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public String dateToString(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(formatter);
    }

    public LocalDateTime stringToDate(String dateTime) {
        if (dateTime == null) {
            return null;
        }
        String[] lines = dateTime.split(" ");
        return LocalDateTime.of(LocalDate.parse(lines[0]), LocalTime.parse(lines[1]));
    }

    public void checkEventDateIsBeforeTwoHours(String eventDate) {
        LocalDateTime dateTime = stringToDate(eventDate);
        if (dateTime.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidatorExceptions("Событие не может быть создано, до его начала меньше 2 часов!");
        }
    }

    public void checkEventDateIsBeforeOneHours(LocalDateTime eventDate) {
        if (eventDate.isBefore(LocalDateTime.now().plusHours(1))) {
            throw new ValidatorExceptions("Событие не может быть опубликовано, до его начала меньше 1 часа!");
        }
    }
}