package com.ncedu.fooddelivery.api.v1.errors;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.ncedu.fooddelivery.api.v1.errors.notfound.NotFoundEx;
import com.ncedu.fooddelivery.api.v1.errors.security.CustomAccessDeniedException;
import com.ncedu.fooddelivery.api.v1.errors.wrappers.ApiError;
import com.ncedu.fooddelivery.api.v1.errors.wrappers.ApiSubError;
import com.ncedu.fooddelivery.api.v1.errors.wrappers.ValidationSubError;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundEx.class)
    protected ResponseEntity<?> handleNotFoundExceptions(
            NotFoundEx ex) {
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, ex.getMessage(), ex.getUuid()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(
            Exception ex, WebRequest request) {
        CustomAccessDeniedException accessEx = new CustomAccessDeniedException();
        return buildResponseEntity(new ApiError(HttpStatus.FORBIDDEN, accessEx.getMessage(), accessEx.getUuid()));
    }

    // in case of invalid type of properties
    @ExceptionHandler(InvalidFormatException.class)
    protected ResponseEntity<?> handleInvalidFormatException(
            InvalidFormatException invalidFormatException) {

        final String UUID = "9ff6f740-3f66-42d9-b6c2-06e79691ef9e";
        final String mainMessage = "Properties have incorrect type.";

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, mainMessage, UUID);
        return buildResponseEntity(apiError);
    }


    // in case of unknown fields
    @ExceptionHandler(UnrecognizedPropertyException.class)
    protected ResponseEntity<?> handleUnrecognizedPropertyException(
            UnrecognizedPropertyException unrecognizedPropertyException) {

        final String UUID = "ba4382b3-10e4-47ab-a8e7-ffd10579b553";
        final String mainMessage = "Unknown properties are not allowed.";

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, mainMessage, UUID);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> handleNotValidJSONException(
            MethodArgumentNotValidException notValidEx) {

        final String UUID = "6ecfc3db-e67c-41c0-a6fd-468d78c2fa26";
        final String mainMessage = "JSON not Valid";
        //create main error wrapper for validation error
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, mainMessage, UUID);

        //create validation reasons and create sub errors
        List<ApiSubError> validationErrors = getValidationErrors(notValidEx);

        apiError.setSubErrors(validationErrors);

        return buildResponseEntity(apiError);
    }

    private List<ApiSubError> getValidationErrors(MethodArgumentNotValidException notValidEx) {
        List<ApiSubError> validationErrors = new ArrayList<>();
        List<ObjectError> objectErrors = notValidEx.getBindingResult().getAllErrors();
        for (ObjectError objectError : objectErrors) {
            validationErrors.add(createValidationSubError(objectError));
        }
        return validationErrors;
    }

    private ValidationSubError createValidationSubError(ObjectError objectError) {
        FieldError fieldError = (FieldError) objectError;
        String objectName = fieldError.getObjectName();
        String fieldName = fieldError.getField();
        Object rejectedValue = fieldError.getRejectedValue();
        String errorMessage = fieldError.getDefaultMessage();
        return new ValidationSubError(objectName, fieldName, rejectedValue, errorMessage);
    }


    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
