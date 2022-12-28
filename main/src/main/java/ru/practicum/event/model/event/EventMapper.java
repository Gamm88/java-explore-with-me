package ru.practicum.event.model.event;

import org.springframework.stereotype.Component;
import ru.practicum.category.model.Category;
import ru.practicum.category.model.CategoryDto;
import ru.practicum.components.DateUtility;
import ru.practicum.user.model.User;
import ru.practicum.user.model.UserShortDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class EventMapper {
    private final DateUtility dateUtility = new DateUtility();

    //из NewEventDto в Event
    public Event mapToEvent(User user, EventNewDto eventNewDto, Category category) {
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
    public EventShortDto mapToEventShortDto(Event event) {
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
    public EventFullDto mapToEventFullDto(Event event) {
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
    public List<EventShortDto> mapToEventShortDto(Iterable<Event> events) {
        List<EventShortDto> dtos = new ArrayList<>();
        for (Event event : events) {
            dtos.add(mapToEventShortDto(event));
        }
        return dtos;
    }

    //получение списка EventFullDto из списка Event
    public List<EventFullDto> mapToEventFullDto(Iterable<Event> events) {
        List<EventFullDto> dtos = new ArrayList<>();
        for (Event event : events) {
            dtos.add(mapToEventFullDto(event));
        }
        return dtos;
    }
}