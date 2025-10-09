package dev.breakin.application.config;

import dev.breakin.exception.ApplicationException;
import dev.breakin.exception.BadRequestException;
import dev.breakin.exception.ClientException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Validation 실패 처리 (@Valid, @Validated)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("status", HttpStatus.BAD_REQUEST.value());
        errors.put("error", "Validation Failed");

        Map<String, String> fieldErrors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });

        errors.put("fields", fieldErrors);
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * BadRequestException 처리
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequestException(BadRequestException e) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", HttpStatus.BAD_REQUEST.value());
        error.put("error", e.getClass().getSimpleName());
        error.put("message", e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    /**
     * ClientException 처리 (400번대)
     */
    @ExceptionHandler(ClientException.class)
    public ResponseEntity<Map<String, Object>> handleClientException(ClientException e) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", e.getStatus());
        error.put("error", e.getClass().getSimpleName());
        error.put("message", e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    /**
     * ApplicationException 처리 (500번대)
     */
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<Map<String, Object>> handleApplicationException(ApplicationException e) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", e.getStatus());
        error.put("error", e.getClass().getSimpleName());
        error.put("message", e.getMessage());
        return ResponseEntity.internalServerError().body(error);
    }

    /**
     * RuntimeException 처리 (fallback)
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException e) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.put("error", "RuntimeException");
        error.put("message", e.getMessage());
        return ResponseEntity.internalServerError().body(error);
    }
}

