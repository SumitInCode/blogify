package com.ssuamkiett.blogify.dto;

import com.ssuamkiett.blogify.controllers.FeedbackRequest;
import com.ssuamkiett.blogify.models.Blog;
import com.ssuamkiett.blogify.models.Feedback;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FeedbackMapper {
    public Feedback toFeedback(FeedbackRequest feedbackRequest) {
        return Feedback.builder()
                .note(feedbackRequest.note())
                .comment(feedbackRequest.comment())
                .blog(Blog.builder()
                        .id(feedbackRequest.blogId())
                        .build())
                .build();
    }
    public FeedbackResponse toFeedbackResponse(Feedback feedback, Long id) {
        return FeedbackResponse.builder()
                .note(feedback.getNote())
                .comment(feedback.getComment())
                .ownFeedback(Objects.equals(feedback.getCreatedBy(), id))
                .build();
    }
}
