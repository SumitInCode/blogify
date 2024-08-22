package com.ssuamkiett.blogify.repositories;

import com.ssuamkiett.blogify.models.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    @Query("""
            SELECT feedback
            FROM Feedback feedback
            WHERE feedback.blog.id = :blogId
            """)
    Page<Feedback> findAllByBlogId(Long blogId, Pageable pageable);
}
