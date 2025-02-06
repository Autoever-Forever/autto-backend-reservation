package com.autto.autto_reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class ApiResponse<T> {

    private boolean success; //성공
    private String message; // 메시지
    private String error; // 에러 코드
    private T data; // 응답 데이터(성공시)

    // 성공 응답 메서드
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, null, data);
    }

    // 실패 응답 메서드
    public static <T> ApiResponse<T> failure(String error, String message) {
        return new ApiResponse<>(false, message, error, null);
    }
}
