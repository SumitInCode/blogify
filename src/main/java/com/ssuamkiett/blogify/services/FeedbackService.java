package com.ssuamkiett.blogify.services;

import com.ssuamkiett.blogify.controllers.FeedbackRequest;
import com.ssuamkiett.blogify.dto.FeedbackMapper;
import com.ssuamkiett.blogify.dto.FeedbackResponse;
import com.ssuamkiett.blogify.dto.PageResponse;
import com.ssuamkiett.blogify.exception.OperationNotPermittedException;
import com.ssuamkiett.blogify.models.Blog;
import com.ssuamkiett.blogify.models.Feedback;
import com.ssuamkiett.blogify.models.User;
import com.ssuamkiett.blogify.repositories.BlogRepository;
import com.ssuamkiett.blogify.repositories.FeedbackRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final BlogRepository blogRepository;
    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;

    public Long save(FeedbackRequest feedbackRequest, Authentication connectedUser) {
        Blog blog = blogRepository.findById(feedbackRequest.blogId())
                .orElseThrow(() -> new EntityNotFoundException("No blog found with the ID : " + feedbackRequest.blogId()));

        User user = (User) connectedUser.getPrincipal();
        if(Objects.equals(blog.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("You cannot give a feedback to your own blog");
        }
        Feedback feedback = feedbackMapper.toFeedback(feedbackRequest);
        return feedbackRepository.save(feedback).getId();
    }

    public PageResponse<FeedbackResponse> findAllFeedbacksByBlog(Long blogId, int page, int size, Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page, size);
        User user = (User) connectedUser.getPrincipal();
        Page<Feedback> feedbacks = feedbackRepository.findAllByBlogId(blogId, pageable);
        List<FeedbackResponse> feedbackResponses = feedbacks.stream()
                .map(feedback -> feedbackMapper.toFeedbackResponse(feedback, user.getId()))
                .toList();
        return new PageResponse<>(
                feedbackResponses,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast()
        );
    }
}
