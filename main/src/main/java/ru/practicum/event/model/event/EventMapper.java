package ru.practicum.event.model.event;

import ru.practicum.user.model.*;
import ru.practicum.category.model.*;
import ru.practicum.components.DateUtility;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

public class EventMapper {
    private static final DateUtility dateUtility = new DateUtility();

    //из NewEventDto в Event
    public static Event mapToEvent(User user, EventNewDto eventNewDto, Category category) {
        return Event.builder()
                .id(eventNewDto.getEventId())
                .title(eventNewDto.getTitle())
                .annotation(eventNewDto.getAnnotation())
                .category(category)
                .paid(eventNewDto.getPaid())
                .eventDate(dateUtility.stringToDate(eventNewDto.getEventDate()))
                .initiator(user)
                .description(eventNewDto.getDescription())
                .participantLimit(eventNewDto.getParticipantLimit())
                .state(State.PENDING)
                .createdOn(LocalDateTime.now())
                .location(eventNewDto.getLocation())
                .requestModeration(eventNewDto.getRequestModeration())
                .confirmedRequests(0)
                .views(0)
                .build();
    }

    //из Event в EventShortDto
    public static EventShortDto mapToEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .category(new CategoryDto(event.getCategory().getId(), event.getCategory().getName()))
                .paid(event.getPaid())
                .eventDate(dateUtility.dateToString(event.getEventDate()))
                .initiator(new UserShortDto(event.getInitiator().getId(), event.getInitiator().getName()))
                .confirmedRequests(0)
                .views(0)
                .build();
    }

    //из Event в EventFullDto
    public static EventFullDto mapToEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .category(new CategoryDto(event.getCategory().getId(), event.getCategory().getName()))
                .paid(event.getPaid())
                .eventDate(dateUtility.dateToString(event.getEventDate()))
                .initiator(new UserShortDto(event.getInitiator().getId(), event.getInitiator().getName()))
                .description(event.getDescription())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(dateUtility.dateToString(event.getPublishedOn()))
                .state(event.getState().toString())
                .createdOn(dateUtility.dateToString(event.getCreatedOn()))
                .location(event.getLocation())
                .requestModeration(event.getRequestModeration())
                .confirmedRequests(0)
                .views(0)
                .build();
    }

    //получение списка EventShortDto из списка Event
    public static List<EventShortDto> mapToEventShortDto(Iterable<Event> events) {
        List<EventShortDto> dtos = new ArrayList<>();
        for (Event event : events) {
            dtos.add(mapToEventShortDto(event));
        }
        return dtos;
    }

    //получение списка EventFullDto из списка Event
    public static List<EventFullDto> mapToEventFullDto(Iterable<Event> events) {
        List<EventFullDto> dtos = new ArrayList<>();
        for (Event event : events) {
            dtos.add(mapToEventFullDto(event));
        }
        return dtos;
    }
}