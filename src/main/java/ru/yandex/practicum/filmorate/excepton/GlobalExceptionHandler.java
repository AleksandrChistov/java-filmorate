package ru.yandex.practicum.filmorate.excepton;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return getErrorResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
        return getErrorResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({Throwable.class})
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Throwable e) {
        return getErrorResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> getErrorResponseEntity(String message, HttpStatus status) {
        ErrorResponse errorResponse = new ErrorResponse(message, status);
        return new ResponseEntity<>(errorResponse, status);
    }

}
