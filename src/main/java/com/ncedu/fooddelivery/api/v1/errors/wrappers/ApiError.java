package com.ncedu.fooddelivery.api.v1.errors.wrappers;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.List;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

@Data
public class ApiError {

    private HttpStatus status;
    private String errorUUID;
    private String dateTime;
    private String message;
    private String debugMessage;
    private List<ApiSubError> subErrors;

    private ApiError() {
        dateTime = createDateTimeString();
    }

    private String createDateTimeString() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.format(ISO_LOCAL_DATE_TIME);
    }

    public ApiError(HttpStatus status) {
        this();
        this.status = status;
    }

    public ApiError(HttpStatus status, String message) {
        this(status);
        this.message = message;
    }

    public ApiError(HttpStatus status, String message, String uuid) {
        this(status, message);
        this.errorUUID = uuid;
    }

    public ApiError(HttpStatus status, Throwable ex) {
        this(status);
        this.message = "Unexpected error";
        this.debugMessage = ex.getLocalizedMessage();
    }

    public ApiError(HttpStatus status, String message, Throwable ex) {
        this(status, message);
        this.debugMessage = stackTraceToString(ex);
    }

    public ApiError(HttpStatus status, String message, String uuid, Throwable ex) {
        this(status, message, uuid);
        this.debugMessage = stackTraceToString(ex);
    }

    private String stackTraceToString(Throwable exception) {
        // converting the stack trace to String
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        exception.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
