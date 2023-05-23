package com.weatherforecast.service.exceptions;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExceptionResponse {
    Integer statusCode;
    String message;
    Date timeStamp;
}
