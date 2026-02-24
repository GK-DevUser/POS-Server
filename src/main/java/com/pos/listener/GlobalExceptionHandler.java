package com.pos.listener;

import com.pos.dto.ApiResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handle(Exception ex) {
        return new ApiResponse<>(ex.getMessage(), false, null);
    }
}
