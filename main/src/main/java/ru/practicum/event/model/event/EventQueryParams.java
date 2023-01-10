package ru.practicum.event.model.event;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventQueryParams {
    // text string - текст для поиска в содержимом аннотации и подробном описании мероприятия.
    private String text;

    // users array - список id пользователей, чьи мероприятия нужно найти.
    private Long[] users;

    // states array - список состояний в которых находятся искомые мероприятия.
    private String[] states;

    // categories array - список id категорий в которых будет вестись поиск.
    private Long[] categories;

    // paid boolean - поиск только платных/бесплатных событий, значение по умолчанию: false
    private Boolean paid = false;

    // rangeStart string - дата и время не раньше которых должно произойти мероприятие.
    private String rangeStart;

    // rangeEnd string - дата и время не позже которых должно произойти мероприятие.
    private String rangeEnd;

    // onlyAvailable boolean - только мероприятия у которых не исчерпан лимит запросов на участие,
    // значение по умолчанию: false
    private boolean onlyAvailable = false;

    // sort string - Вариант сортировки: по дате мероприятия или по количеству просмотров,
    // возможные значения: EVENT_DATE, VIEWS.
    private String sort;

    // from integer - количество событий, которые нужно пропустить для формирования текущего набора,
    // значение по умолчанию: 0.
    private Integer from = 0;

    // size integer - количество событий в наборе, значение по умолчанию: 10.
    private Integer size = 10;
}