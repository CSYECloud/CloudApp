package com.hari.cloud.app.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.FileNotFoundException;
import java.net.ConnectException;
import java.nio.file.NoSuchFileException;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleUncaughtException(RuntimeException exception, HttpServletRequest request) {
        String message = "Unknown error occured";
        System.out.println(exception.getStackTrace());
        return buildResponseEntity(new APIError(HttpStatus.INTERNAL_SERVER_ERROR, message, exception));
    }

    @ExceptionHandler({PSQLException.class, ConnectException.class})
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<Object> handlePSQLException(RuntimeException exception, HttpServletRequest request) {
        return new ResponseEntity(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(MethodNotAllowedException.class)
    public ResponseEntity<String> handleMethodNotAllowed(MethodNotAllowedException ex) {
        return new ResponseEntity(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler({IllegalArgumentException.class, FileNotFoundException.class, NoSuchFileException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleNotFoundException(Exception exception, HttpServletRequest request) {
        String message = "Requested resource could not be located";
        return buildResponseEntity(new APIError(HttpStatus.NOT_FOUND, message, exception));
    }

    private ResponseEntity<Object> buildResponseEntity(APIError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
