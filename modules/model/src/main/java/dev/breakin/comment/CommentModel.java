package dev.breakin.comment;

import dev.breakin.AuditProps;
import dev.breakin.common.CommentOrder;

public interface CommentModel extends AuditProps {
    Long getCommentId();
    Long getUserId();
    String getContent();
    String getTargetType();
    Long getTargetId();
    Long getParentId();
    CommentOrder getCommentOrder();
    Boolean getIsHidden();
}
