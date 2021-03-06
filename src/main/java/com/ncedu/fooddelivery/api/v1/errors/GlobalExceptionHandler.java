package com.ncedu.fooddelivery.api.v1.errors;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.ncedu.fooddelivery.api.v1.errors.badrequest.*;
import com.ncedu.fooddelivery.api.v1.errors.notfound.NotFoundEx;
import com.ncedu.fooddelivery.api.v1.errors.orderRegistration.CourierAvailabilityEx;
import com.ncedu.fooddelivery.api.v1.errors.orderRegistration.OrderCostChangedEx;
import com.ncedu.fooddelivery.api.v1.errors.orderRegistration.ProductAvailabilityEx;
import com.ncedu.fooddelivery.api.v1.errors.orderRegistration.WarehouseCoordsBindingEx;
import com.ncedu.fooddelivery.api.v1.errors.security.CustomAccessDeniedException;
import lombok.extern.slf4j.Slf4j;
import com.ncedu.fooddelivery.api.v1.errors.wrappers.*;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
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
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundEx.class)
    protected ResponseEntity<?> handleNotFoundExceptions(
            NotFoundEx ex) {
        log.error(ex.getMessage(), ex);
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
        log.error(notValidEx.getMessage(), notValidEx);
        final String UUID = "6ecfc3db-e67c-41c0-a6fd-468d78c2fa26";
        final String mainMessage = "JSON not Valid";
        //create main error wrapper for validation error
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, mainMessage, UUID);

        //create validation reasons and create sub errors
        List<ApiSubError> validationErrors = getValidationErrors(notValidEx);

        apiError.setSubErrors(validationErrors);

        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(BindException.class)
    protected ResponseEntity<?> handleBindException(BindException ex) {
        log.error(ex.getMessage(), ex);
        final String UUID = "6ecfc3db-e67c-41c0-a6fd-468d78c2fa26";
        final String mainMessage = "Parameters not valid!";
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, mainMessage, UUID);
        List<ApiSubError> validationErrors = getValidationErrors(ex);
        apiError.setSubErrors(validationErrors);
        return buildResponseEntity(apiError);
    }

    private List<ApiSubError> getValidationErrors(BindException notValidEx) {
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

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<Object> handleAlreadyExistsException(
            AlreadyExistsException ex) {
        log.error(ex.getMessage(), ex);
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getUuid()));
    }

    @ExceptionHandler(PasswordsMismatchException.class)
    public ResponseEntity<Object> handlePasswordsMismatchException(
            PasswordsMismatchException ex) {
        log.error(ex.getMessage(), ex);
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getUuid()));
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<Object> handleMissingPathVariableException(
            MissingPathVariableException ex) {
        log.error(ex.getMessage(), ex);
        final String UUID = "e547f7c0-352e-4798-9def-c716f1288b02";
        final String message = "Path var not presented or value of var is bad";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, message, UUID));
    }

    @ExceptionHandler(BadFileExtensionException.class)
    public ResponseEntity<Object> handleBadFileExtensionException(
            BadFileExtensionException ex) {
        log.error(ex.getMessage(), ex);
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getUuid()));
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<Object> handleFileStorageException(
            FileStorageException ex) {
        log.error(ex.getMessage(), ex);
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getUuid()));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Object> handleMaxUploadSizeException(
            MaxUploadSizeExceededException ex) {
        log.error(ex.getMessage(), ex);
        final String UUID = "e32be0ba-48e8-4b30-ad79-5723c9d5fa14";
        final String message = "File size exceeded";
        return buildResponseEntity(new ApiError(HttpStatus.EXPECTATION_FAILED, message, UUID));
    }

    @ExceptionHandler(FileDeleteException.class)
    public ResponseEntity<Object> handleFileDeleteException(
            FileDeleteException ex) {
        log.error(ex.getMessage(), ex);
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getUuid()));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handleNullPointerException(
            NullPointerException ex) {
        log.error(ex.getMessage(), ex);
        final String UUID = "3aef8117-7459-4366-aece-3c20d57bbb25";
        final String message = "Request data can't be null";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, message, UUID));
    }

    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<Object> handleRefreshTokenException(
            RefreshTokenException ex) {
        log.error(ex.getMessage(), ex);
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getUuid()));

    }
  
    @ExceptionHandler(ProductAvailabilityEx.class)
    public ResponseEntity<Object> handleProductAvailabilityEx(ProductAvailabilityEx ex){
        ApiError err = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage(), ex.uuid);
        List<ApiSubError> subErrors = new ArrayList<>();
        err.setSubErrors(subErrors);

        for(Map.Entry<Long, Integer> entry: ex.getProductsAvailableAmount().entrySet()){
                subErrors.add(new ProductNotEnoughSubError(entry.getKey(), entry.getValue()));
        }
        return buildResponseEntity(err);
    }

    @ExceptionHandler(CourierAvailabilityEx.class)
    public ResponseEntity<Object> handleCourierAvailabilityEx(CourierAvailabilityEx ex){
        ApiError err = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage(), CourierAvailabilityEx.uuid);
        return buildResponseEntity(err);
    }

    @ExceptionHandler(OrderCostChangedEx.class)
    public ResponseEntity<Object> handleOrderCostChangedEx(OrderCostChangedEx ex){
        ApiError err = new ApiError(HttpStatus.NOT_ACCEPTABLE, ex.getMessage(), OrderCostChangedEx.uuid);
        err.setSubErrors(new ArrayList<>(Arrays.asList(new OrderCostChangedSubError(ex.getOverallCost(),
                ex.getDiscount(), ex.getHighDemandCoeff()))));
        return buildResponseEntity(err);
    }

    @ExceptionHandler(OrderStatusChangeException.class)
    public ResponseEntity<Object> handleOrderStatusChangeEx(OrderStatusChangeException ex){
        ApiError err = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), OrderStatusChangeException.uuid);
        return buildResponseEntity(err);
    }

    @ExceptionHandler(CourierNotSetException.class)
    public ResponseEntity<Object> handleCourierNotSetException(CourierNotSetException ex){
        ApiError err = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), CourierNotSetException.uuid);
        return buildResponseEntity(err);
    }

    @ExceptionHandler(CourierReplaceException.class)
    public ResponseEntity<Object> handleCourierReplaceException(CourierReplaceException ex){
        ApiError err = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), CourierReplaceException.uuid);
        return buildResponseEntity(err);
    }

    @ExceptionHandler(WarehouseCoordsBindingEx.class)
    public ResponseEntity<Object> handleWarehouseCoordsBindingEx(WarehouseCoordsBindingEx ex){
        ApiError err = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), WarehouseCoordsBindingEx.uuid);
        return buildResponseEntity(err);
    }

    @ExceptionHandler(DeliverySessionAlreadyStartedException.class)
    public ResponseEntity<Object> handleDeliverySessionAlreadyStartedException(DeliverySessionAlreadyStartedException ex){
        log.error(com.ncedu.fooddelivery.api.v1.errors.badrequest.DeliverySessionAlreadyStartedException.msg, ex);
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, com.ncedu.fooddelivery.api.v1.errors.badrequest.DeliverySessionAlreadyStartedException.msg,
                com.ncedu.fooddelivery.api.v1.errors.badrequest.DeliverySessionAlreadyStartedException.uuid));
    }

    @ExceptionHandler(DeliverySessionAlreadyFinishedException.class)
    public ResponseEntity<Object> handleDeliverySessionAlreadyFinishedException(DeliverySessionAlreadyFinishedException ex){
        log.error(ex.getMessage(), ex);
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(),
                DeliverySessionAlreadyFinishedException.uuid));
    }

    @ExceptionHandler(DeliverySessionFinishException.class)
    public ResponseEntity<Object> handleDeliverySessionFinishException(DeliverySessionFinishException ex){
        log.error(ex.getMessage(), ex);
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(),
                DeliverySessionFinishException.uuid));
    }


    @ExceptionHandler(NoActiveDeliverySessionException.class)
    public ResponseEntity<Object> handleNoActiveDeliverySessionException(NoActiveDeliverySessionException ex){
        log.error(NoActiveDeliverySessionException.msg, ex);
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, NoActiveDeliverySessionException.msg,
                NoActiveDeliverySessionException.uuid));
    }
  
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleNotProcessedException(
            Exception ex) {
        log.error(ex.getMessage(), ex);
        final String UUID = "3155227c-2001-4878-b2e7-040c4d4d803c";
        final String message = "Unknown exception. Internal server error";
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, message, UUID, ex));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
