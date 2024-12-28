package com.autto.back_reservation.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiResponse<T> {
    private boolean success; //성공 여부
    private String message; //메시지
    private String error; //에러 코드(실패 시)
    private T data; //으압 데이터(성공 시)

    private ApiResponse(boolean success, String message, String error, T data) {
        this.success = success;
        this.message = message;
        this.error = error;
        this.data = data;
    }

    // 성공 응답 메서드
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, null, data);
    }

    // 실패 응답 팩토리 메서드
    public static <T> ApiResponse<T> failure(String error, String message){
        return new ApiResponse<>(false, message, error, null);
    }

}
