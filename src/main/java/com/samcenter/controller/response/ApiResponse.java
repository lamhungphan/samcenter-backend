package com.samcenter.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> implements Serializable {
    private int status;
    private String message;
    private transient T data;

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(200, message, data);
    }

    public static <T> ApiResponse<T> error(String message, int status) {
        return new ApiResponse<>(status, message, null);
    }
}
