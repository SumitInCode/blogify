package com.ssuamkiett.blogify.controllers;

import com.ssuamkiett.blogify.dto.FeedbackResponse;
import com.ssuamkiett.blogify.dto.PageResponse;
import com.ssuamkiett.blogify.services.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<Long> saveFeedback(
            @Valid @RequestBody FeedbackRequest feedbackRequest,
            Authentication connectedUser) {
        return ResponseEntity.ok(feedbackService.save(feedbackRequest, connectedUser));
    }

    @GetMapping("/blog/{blog-id}")
    public ResponseEntity<PageResponse<FeedbackResponse>> findAllFeedbacksByBook(
            @PathVariable("blog-id") Long blogId,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(feedbackService.findAllFeedbacksByBlog(blogId, page, size, connectedUser));
    }
}
