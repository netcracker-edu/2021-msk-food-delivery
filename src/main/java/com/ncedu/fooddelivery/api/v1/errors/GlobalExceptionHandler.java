package com.ncedu.fooddelivery.api.v1.errors;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.ncedu.fooddelivery.api.v1.errors.badrequest.AlreadyExistsException;
import com.ncedu.fooddelivery.api.v1.errors.badrequest.PasswordsMismatchException;
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
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
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

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex){
        final String mainMessage = "Type mismatch. Param: {" + ex.getName() + "}; Value: {" + ex.getValue().toString() + "}.";
        final String UUID = "50b8b93f-86d1-48e3-b271-d7107a2a900f";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, mainMessage, UUID));
    }

    @ExceptionHandler(UnrecognizedPropertyException.class)
    protected ResponseEntity<Object> handleUnrecognizedPropertyException(UnrecognizedPropertyException ex){
        final String mainMessage = "Unknown fields aren't allowed. Field: {" + ex.getPropertyName() + "}.";
        final String UUID = "1c8b3f40-ecd7-4822-a1f8-58212664a7fa";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, mainMessage, UUID));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex){
        ConstraintViolation cv = ex.getConstraintViolations().iterator().next();
        String fullParam = cv.getPropertyPath().toString();
        String param = fullParam.substring(fullParam.lastIndexOf('.') + 1);
        String value = cv.getInvalidValue().toString();

        final String mainMessage = "Constraint violation! Param: {" + param + "}; Value: {" + value + "}.";
        final String UUID = "2a7b40a2-faf8-4a6c-b6a5-84fb7162f8b7";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, mainMessage, UUID));
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

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(
            Exception ex, WebRequest request) {
        CustomAccessDeniedException accessEx = new CustomAccessDeniedException();
        return buildResponseEntity(new ApiError(HttpStatus.FORBIDDEN, accessEx.getMessage(), accessEx.getUuid()));
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<Object> handleAlreadyExistsException(
            AlreadyExistsException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getUuid()));
    }

    @ExceptionHandler(PasswordsMismatchException.class)
    public ResponseEntity<Object> handlePasswordsMismatchException(
            PasswordsMismatchException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getUuid()));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
