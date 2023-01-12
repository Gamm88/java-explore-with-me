package ru.practicum.components;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Component
public class DateUtility {

    public LocalDateTime stringToDate(String dateTime) {
        if (dateTime == null) {
            return null;
        }
        String[] lines = dateTime.split(" ");
        return LocalDateTime.of(LocalDate.parse(lines[0]), LocalTime.parse(lines[1]));
    }
}