package com.ssuamkiett.blogify.dto;

import com.ssuamkiett.blogify.models.Blog;
import com.ssuamkiett.blogify.sanitizers.SanitizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlogMapper {
    private final SanitizationService sanitizationService;

    public Blog toBlog(BlogRequest blogRequest) {
        return Blog.builder()
                .title(sanitizationService.sanitize(blogRequest.title()))
                .body(sanitizationService.sanitize(blogRequest.body()))
                .tags(blogRequest.tags())
                .build();
    }

    public void updateBlog(Blog blog, BlogRequest blogRequest) {
        blog.setTitle(sanitizationService.sanitize(blogRequest.title()));
        blog.setBody(sanitizationService.sanitize(blogRequest.body()));
        blog.setTags(blog.getTags() + "," + blogRequest.tags());
    }

    public BlogResponse toBlogResponse(Blog blog) {
        return BlogResponse.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .body(blog.getBody())
                .author(blog.getAuthorName())
                .rate(blog.getRate())
                .tags(blog.getTags())
                .build();
    }
}
