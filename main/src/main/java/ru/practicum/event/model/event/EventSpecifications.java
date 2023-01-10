package ru.practicum.event.model.event;

import ru.practicum.components.DateUtility;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.time.LocalDateTime;

/**
 * Создание шаблона спецификации поиска событий, используется для поиска событий в БД, по множеству параметров
 */
public class EventSpecifications {
    private static final DateUtility dateUtility = new DateUtility();

    // создание спецификации по запросу поиска от пользователя
    public static Specification<Event> getUserSpecifications(EventQueryParams criteria) {
        return (textIn("annotation", criteria.getText())
                .or(textIn("description", criteria.getText())))
                .and(idsIn(criteria.getCategories(), "category"))
                .and(isPaid(criteria.getPaid()))
                .and(dateBetween(
                        dateUtility.stringToDate(criteria.getRangeStart()),
                        dateUtility.stringToDate(criteria.getRangeEnd())))
                .and(isAvailable(criteria.isOnlyAvailable()))
                .and(stateIs(List.of(State.PUBLISHED)));
    }

    // создание спецификации по запросу поиска от администратора
    public static Specification<Event> getAdminSpecifications(EventQueryParams criteria) {
        return idsIn(criteria.getUsers(), "initiator")
                .and(stateIs(stateMapping(criteria.getStates())))
                .and(idsIn(criteria.getCategories(), "category"))
                .and(dateBetween(
                        dateUtility.stringToDate(criteria.getRangeStart()),
                        dateUtility.stringToDate(criteria.getRangeEnd())));
    }

    private static Specification<Event> textIn(String field, String text) {
        return (root, query, builder) -> {
            if (text == null || text.length() < 2) {
                return builder.conjunction();
            }
            return builder.like(builder.lower(root.get(field)), "%" + text.toLowerCase() + "%");
        };
    }

    private static Specification<Event> idsIn(Long[] ids, String column) {
        return (root, query, builder) -> {
            if (ids == null) {
                return builder.conjunction();
            }
            return root.get(column).in(Arrays.asList(ids));
        };
    }

    private static Specification<Event> isPaid(boolean paid) {
        return (root, query, builder) -> builder.equal(root.get("paid"), paid);
    }

    private static Specification<Event> dateBetween(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        return (root, query, builder) -> {
            if (rangeStart == null || rangeEnd == null) {
                return builder.greaterThan(root.get("eventDate"), LocalDateTime.now());
            }
            return builder.between(root.get("eventDate"), rangeStart, rangeEnd);
        };
    }

    private static Specification<Event> isAvailable(boolean available) {
        return (root, query, builder) -> {
            if (available) {
                return builder.equal(root.get("confirmedRequests"), root.get("participantLimit"));
            }
            return builder.lessThan(root.get("confirmedRequests"), root.get("participantLimit"));
        };
    }

    private static Specification<Event> stateIs(List<State> states) {
        return (root, query, builder) -> {
            if (states == null) {
                return builder.conjunction();
            }
            return root.get("state").in(states);
        };
    }

    private static List<State> stateMapping(String[] strings) {
        if (strings == null) {
            return null;
        }

        List<State> states = new ArrayList<>();
        for (String string : strings) {
            if (string.equals(State.valueOf(string).toString())) {
                states.add(State.valueOf(string));
            }
        }

        return states;
    }
}