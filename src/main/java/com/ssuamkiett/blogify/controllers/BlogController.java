package com.ssuamkiett.blogify.controllers;

import com.ssuamkiett.blogify.dto.BlogRequest;
import com.ssuamkiett.blogify.dto.BlogResponse;
import com.ssuamkiett.blogify.dto.PageResponse;
import com.ssuamkiett.blogify.services.BlogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/blogs")
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;

    @PostMapping("/create")
    public ResponseEntity<BlogResponse> createPost(@Valid @RequestBody BlogRequest blogRequest, Authentication connectedUser) {
        return ResponseEntity.ok(blogService.createPost(blogRequest, connectedUser));
    }

    @DeleteMapping("/delete/{blogId}")
    public ResponseEntity<?> deletePost(@PathVariable("blogId") Long blogId, Authentication connectedUser) {
        blogService.deletePost(blogId, connectedUser);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/edit/{blogId}")
    public ResponseEntity<BlogResponse> editBlog(@PathVariable("blogId") Long blogId, @Valid @RequestBody BlogRequest blogRequest, Authentication connectedUser) {
        return ResponseEntity.ok().body(blogService.editBlog(blogId, blogRequest, connectedUser));
    }

    @GetMapping
    public ResponseEntity<PageResponse<BlogResponse>> findAllBlogs(
                @RequestParam(name = "page", defaultValue = "0", required = false) int page,
                @RequestParam(name = "size", defaultValue = "10", required = false) int size,
                Authentication connectedUser
        ) {
        return ResponseEntity.ok().body(blogService.findAllBlogs(page, size, connectedUser));
    }

    @GetMapping("/owner")
    public ResponseEntity<PageResponse<BlogResponse>> findAllBlogsByOwner(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok().body(blogService.findAllBlogsByOwner(page, size, connectedUser));
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<BlogResponse>> findAllBlogsContainingTitle(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "title") String title
    ) {
        return ResponseEntity.ok().body(blogService.findAllBlogsContainingTitle(page, size, title));
    }
}

