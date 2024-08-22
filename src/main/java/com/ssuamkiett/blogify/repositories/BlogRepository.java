package com.ssuamkiett.blogify.repositories;

import com.ssuamkiett.blogify.models.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {
    @Query("""
            SELECT blog
            FROM Blog blog
            WHERE blog.owner.id != :userId
            """)
    Page<Blog> findAllDisplayableBlogs(Pageable pageable, Long userId);

    Page<Blog> findAllBlogsByTitleContainsIgnoreCase(Pageable pageable, String title);
}

