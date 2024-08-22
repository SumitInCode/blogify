package com.ssuamkiett.blogify.specification;

import com.ssuamkiett.blogify.models.Blog;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class BlogSpecification {
    public static Specification<Blog> withOwnerId(Long ownerId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("owner").get("id"), ownerId);
    }

    public static Specification<Blog> hasKeywordInTitle(String keyword) {
        return (Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            String[] keywords = keyword.split("\\s+");
            Predicate predicate = builder.conjunction();
            for (String key : keywords) {
                predicate = builder.and(predicate, builder.like(builder.lower(root.get("title")), "%" + key.toLowerCase() + "%"));
            }
            return predicate;
        };
    }
}
