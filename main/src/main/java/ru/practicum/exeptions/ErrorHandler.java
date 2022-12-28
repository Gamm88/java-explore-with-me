package ru.practicum.exeptions;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MissingServletRequestParameterException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    // Перехват ошибки не найденных данных
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        log.info("NOT_FOUND исключение - " + e.getMessage() + "!");

        return new ErrorResponse(e.getMessage());
    }

    // Перехват ошибки дублирования данных
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateException(final DuplicateException e) {
        log.info("CONFLICT исключение - " + e.getMessage() + "!");

        return new ErrorResponse(e.getMessage());
    }

    // Перехват ошибки некорректного запроса
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidatorException(final ValidatorExceptions e) {
        log.info("BAD_REQUEST исключение - " + e.getMessage() + "!");

        return new ErrorResponse(e.getMessage());
    }

    // Перехват ошибки без обязательного query params
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingParams(MissingServletRequestParameterException e) {
        log.info("BAD_REQUEST исключение - в запросе не указан параметр: " + e.getParameterName() + "!");

        return new ErrorResponse("В запросе не указан параметр: " + e.getParameterName() + "!");
    }

    @Data
    static class ErrorResponse {
        private final String status;

        public ErrorResponse(String error) {
            this.status = error;
        }
    }
}