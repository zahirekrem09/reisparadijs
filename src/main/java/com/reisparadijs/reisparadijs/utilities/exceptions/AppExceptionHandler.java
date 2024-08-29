package com.reisparadijs.reisparadijs.utilities.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reisparadijs.reisparadijs.communication.dto.response.DetailedErrorResponse;
import com.reisparadijs.reisparadijs.communication.dto.response.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.resource.NoResourceFoundException;


import java.util.HashMap;
import java.util.Map;

/**
 * @author Zahir Ekrem SARITEKE
 * @project reisparadijs
 * @created 11 August Sunday 2024 - 21:36
 */


@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class AppExceptionHandler {

    @JsonIgnore
    private final Logger log = LoggerFactory.getLogger(AppExceptionHandler.class);


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public final ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupported(
            final HttpRequestMethodNotSupportedException e) {
        log.error(e.toString(), e.getMessage());
        return build(HttpStatus.METHOD_NOT_ALLOWED, "Request method not supported");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public final ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(final HttpMessageNotReadableException e) {
        log.error(e.toString(), e.getMessage());
        return build(HttpStatus.BAD_REQUEST, "Malformed JSON request");
    }

    @ExceptionHandler(BindException.class)
    public final ResponseEntity<ErrorResponse> handleBindException(final BindException e) {
        log.error(e.toString(), e.getMessage());
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        return build(HttpStatus.BAD_REQUEST, "Validation error", errors);
    }

    @ExceptionHandler({
            MultipartException.class,
            MissingServletRequestPartException.class,
            HttpMediaTypeNotSupportedException.class,
            MethodArgumentTypeMismatchException.class,
            IllegalArgumentException.class,
            InvalidDataAccessApiUsageException.class,
            ConstraintViolationException.class,
            MissingRequestHeaderException.class,
            MalformedJwtException.class,
            UserDisabledException.class,
            BadCredentialsException.class,

    })
    public final ResponseEntity<ErrorResponse> handleBadRequestException(final Exception e) {
        log.error(e.toString(), e.getMessage());
        return build(HttpStatus.BAD_REQUEST, e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
    }

    @ExceptionHandler({NotFoundException.class, ParentMessageNotFoundException.class, NoResourceFoundException.class,AccommodationNotFoundException.class})
    public final ResponseEntity<ErrorResponse> handleNotFoundException(final Exception e) {
        log.error(e.toString(), e.getMessage());
        return build(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(UserAlreadyVerifyException.class)
    public final ResponseEntity<ErrorResponse> handleUserAlreadyVerifyException(final UserAlreadyVerifyException e) {
        log.error(e.toString(), e.getMessage());
        return build(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler({TokenExpiredException.class, ExpiredJwtException.class})
    public final ResponseEntity<ErrorResponse> handleTokenExpiredException(final Exception e) {
        log.error(e.toString(), e.getMessage());
        return build(HttpStatus.UNAUTHORIZED, "Token expired");
    }


    @ExceptionHandler({
            UnauthorizedException.class,
//            BadCredentialsException.class,
//            AuthenticationCredentialsNotFoundException.class
    })
    public final ResponseEntity<ErrorResponse> handleBadCredentialsException(final Exception e) {
        log.error(e.toString(), e.getMessage());
        return build(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<ErrorResponse> handleAccessDeniedException(final Exception e) {
        log.error(e.toString(), e.getMessage());
        return build(HttpStatus.FORBIDDEN, e.getMessage());
    }


    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponse> handleAllExceptions(final Exception e) {
        log.error("Exception: {}", e.getMessage());
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }

    /**
     * Build error response.
     *
     * @param httpStatus HttpStatus enum to response status field
     * @param message    String for response message field
     * @param errors     Map for response errors field
     * @return ResponseEntity
     */
    private ResponseEntity<ErrorResponse> build(final HttpStatus httpStatus,
                                                final String message,
                                                final Map<String, String> errors) {
        if (!errors.isEmpty()) {
            return ResponseEntity.status(httpStatus).body(new DetailedErrorResponse(
                    message,
                    errors)
            );
        }

        return ResponseEntity.status(httpStatus).body(new ErrorResponse(message));
    }

    /**
     * Build error response.
     *
     * @param httpStatus HttpStatus enum to response status field
     * @param message    String for response message field
     * @return ResponseEntity
     */
    private ResponseEntity<ErrorResponse> build(final HttpStatus httpStatus, final String message) {
        return build(httpStatus, message, new HashMap<>());
    }
}
