package dev.breakin.common;

import lombok.Value;

/**
 * 댓글의 계층 구조 및 순서를 관리하는 Value Object
 * 향후 설계 예정
 */
@Value
public class CommentOrder {
    // TODO: 계층 구조 설계 후 필드 추가
    // 예: depth, orderPath 등

    public static CommentOrder empty() {
        return new CommentOrder();
    }
}
