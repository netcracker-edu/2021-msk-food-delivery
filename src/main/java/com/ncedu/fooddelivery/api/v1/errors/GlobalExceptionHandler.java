package com.ncedu.fooddelivery.api.v1.errors;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.ncedu.fooddelivery.api.v1.errors.badrequest.*;
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
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

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

    @ExceptionHandler(IncorrectUserRoleRequestException.class)
    protected ResponseEntity<Object> handleIncorrectUserRoleRequestException(IncorrectUserRoleRequestException ex){
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, IncorrectUserRoleRequestException.message, IncorrectUserRoleRequestException.UUID));
    }

    @ExceptionHandler(IncorrectProductPositionWarehouseBindingException.class)
    protected ResponseEntity<Object> handleIncorrectProductPositionWarehouseBindingException(IncorrectProductPositionWarehouseBindingException ex){
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), IncorrectProductPositionWarehouseBindingException.UUID));
    }

    @ExceptionHandler(NotUniqueIdException.class)
    protected ResponseEntity<Object> handleNotUniqueIdException (NotUniqueIdException ex){
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, NotUniqueIdException.message, NotUniqueIdException.UUID));
    }

    @ExceptionHandler(ProductPositionNotEnoughException.class)
    protected ResponseEntity<Object> handleProductPositionNotEnoughException (ProductPositionNotEnoughException ex){
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), ProductPositionNotEnoughException.UUID));
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

    @ExceptionHandler(FileDeleteException.class)
    public ResponseEntity<Object> handleFileDeleteException(
            FileDeleteException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getUuid()));
    }

    /*@ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handleNullPointerException(
            NullPointerException ex) {
        final String UUID = "3aef8117-7459-4366-aece-3c20d57bbb25";
        final String message = "Request data can't be null";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, message, UUID));
    }*/
  
    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
