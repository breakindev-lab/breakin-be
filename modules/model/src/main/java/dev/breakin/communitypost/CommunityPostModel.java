package dev.breakin.communitypost;

import dev.breakin.AuditProps;
import dev.breakin.common.Popularity;

public interface CommunityPostModel extends AuditProps {
    Long getCommunityPostId();
    Long getUserId();
    String getCategory();
    String getTitle();
    String getMarkdownBody();
    String getCompany();
    String getLocation();
    Long getLinkedJobId();
    Boolean getIsFromJobComment();
    Popularity getPopularity();
    Boolean getIsDeleted();
}
