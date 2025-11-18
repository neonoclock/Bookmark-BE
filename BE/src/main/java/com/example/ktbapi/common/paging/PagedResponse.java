package com.example.ktbapi.common.paging;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "페이지네이션 응답 래퍼")
public record PagedResponse<T>(
        @Schema(description = "현재 페이지(0-base)") int page,
        @Schema(description = "페이지 크기") int size,
        @Schema(description = "전체 요소 수") long totalElements,
        @Schema(description = "다음 페이지 존재 여부") boolean hasNext,
        @Schema(description = "아이템 목록") List<T> items
) {
    public PagedResponse(List<T> items, int page, int size, long totalElements, boolean hasNext) {
        this(page, size, totalElements, hasNext, List.copyOf(items));
    }
}
