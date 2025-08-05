package sk.hruby.michal.usersandpolicies.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import sk.hruby.michal.usersandpolicies.exception.AlreadyExistsException;
import sk.hruby.michal.usersandpolicies.exception.BadRequestException;
import sk.hruby.michal.usersandpolicies.exception.InvalidJsonException;
import sk.hruby.michal.usersandpolicies.exception.NotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> handleGenericException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage,
                        (msg1, msg2) -> msg1
                ));

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BadRequestException.class, ConstraintViolationException.class})
    public ResponseEntity handleBadRequestException(Exception e) {
        Map<String, String> errorBody = new HashMap<>();
        errorBody.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException(Exception e) {
        Map<String, String> errorBody = new HashMap<>();
        errorBody.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity handleAlreadyExist(AlreadyExistsException e) {
        Map<String, String> errorBody = new HashMap<>();
        errorBody.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorBody);
    }

    @ExceptionHandler(InvalidJsonException.class)
    public ResponseEntity handleInvalidJson(InvalidJsonException e) {
        Map<String, String> errorBody = new HashMap<>();
        errorBody.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNotFound(NoHandlerFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Oops! The path '" + ex.getRequestURL() + "' does not exist.");
    }
}
