package ru.practicum.request.model;

import ru.practicum.components.DateUtility;

import java.util.List;
import java.util.ArrayList;

public class RequestMapper {
    private static final DateUtility dateUtility = new DateUtility();

    //из Request в RequestDto
    public static RequestDto mapToRequestDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .created(dateUtility.dateToString(request.getCreated()))
                .status(request.getStatus().toString())
                .build();
    }

    //получение списка RequestDto из списка Request
    public static List<RequestDto> mapToRequestDto(Iterable<Request> requests) {
        List<RequestDto> dtos = new ArrayList<>();
        for (Request request : requests) {
            dtos.add(mapToRequestDto(request));
        }
        return dtos;
    }
}