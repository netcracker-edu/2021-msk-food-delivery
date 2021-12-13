package com.ncedu.fooddelivery.api.v1.errors;

import com.ncedu.fooddelivery.api.v1.errors.badrequest.AlreadyExistsException;
import com.ncedu.fooddelivery.api.v1.errors.badrequest.BadFileExtensionException;
import com.ncedu.fooddelivery.api.v1.errors.badrequest.FileStorageException;
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
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

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

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<Object> handleMissingPathVariableException(
            MissingPathVariableException ex) {
        final String UUID = "e547f7c0-352e-4798-9def-c716f1288b02";
        final String message = "Path var not presented or value of var is bad";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, message, UUID));
    }

    @ExceptionHandler(BadFileExtensionException.class)
    public ResponseEntity<Object> handleBadFileExtensionException(
            BadFileExtensionException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getUuid()));
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<Object> handleFileStorageException(
            FileStorageException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getUuid()));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Object> handleMaxUploadSizeException(
            MaxUploadSizeExceededException ex) {
        final String UUID = "e32be0ba-48e8-4b30-ad79-5723c9d5fa14";
        final String message = "File size exceeded";
        return buildResponseEntity(new ApiError(HttpStatus.EXPECTATION_FAILED, message, UUID));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
