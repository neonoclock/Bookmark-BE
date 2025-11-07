package com.example.ktbapi.common;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final boolean success;
    private final String code;     // "OK" | "not_found" | ...
    private final T data;          // 성공 시 payload
    private final Object detail;   // 실패 시 부가정보(Map, String 등 아무 타입)

    private ApiResponse(boolean success, String code, T data, Object detail) {
        this.success = success;
        this.code = code;
        this.data = data;
        this.detail = detail;
    }

    // ---- success ----
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "OK", data, null);
    }
    public static ApiResponse<Void> success() {
        return new ApiResponse<>(true, "OK", null, null);
    }

    // ---- fail (기존 스타일) ----
    public static ApiResponse<Void> fail(String code) {
        return new ApiResponse<>(false, code, null, null);
    }
    public static ApiResponse<Void> fail(String code, Object detail) {
        return new ApiResponse<>(false, code, null, detail);
    }

    // ---- error (호환용 별칭) ----
    public static ApiResponse<Void> error(String code) {
        return new ApiResponse<>(false, code, null, null);
    }
    public static ApiResponse<Void> error(String code, Object detail) {
        return new ApiResponse<>(false, code, null, detail);
    }

    // ---- getters ----
    public boolean isSuccess() { return success; }
    public String getCode() { return code; }
    public T getData() { return data; }
    public Object getDetail() { return detail; }
}
