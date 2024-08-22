package com.ssuamkiett.blogify.services;

import com.ssuamkiett.blogify.dto.BlogMapper;
import com.ssuamkiett.blogify.dto.BlogRequest;
import com.ssuamkiett.blogify.dto.BlogResponse;
import com.ssuamkiett.blogify.dto.PageResponse;
import com.ssuamkiett.blogify.exception.OperationNotPermittedException;
import com.ssuamkiett.blogify.models.Blog;
import com.ssuamkiett.blogify.models.User;
import com.ssuamkiett.blogify.repositories.BlogRepository;
import com.ssuamkiett.blogify.specification.BlogSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;
    private final BlogMapper blogMapper;

    public BlogResponse createPost(BlogRequest blogRequest, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Blog blog = blogMapper.toBlog(blogRequest);
        blog.setAuthorName(user.getFullName());
        blog.setOwner(user);
        Long postId = blogRepository.save(blog).getId();
        return BlogResponse.builder()
                .id(postId)
                .build();
    }

    public void deletePost(Long blogId, Authentication connectedUser) {
        Blog blog = getPostFromDB(blogId);
        User user = (User) connectedUser.getPrincipal();
        if(!Objects.equals(blog.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("Operation Not permitted to delete post");
        }
        blogRepository.delete(blog);
    }

    private Blog getPostFromDB(Long blogId) {
        return blogRepository.findById(blogId)
                .orElseThrow(() -> new EntityNotFoundException("No post found with the ID : " + blogId));
    }

    public BlogResponse editBlog(Long blogId, BlogRequest blogRequest, Authentication connectedUser) {
        Blog blog = getPostFromDB(blogId);
        User user = (User) connectedUser.getPrincipal();
        if(!Objects.equals(blog.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedException("Operation Not permitted to edit post");
        }
        blogMapper.updateBlog(blog, blogRequest);
        blogRepository.save(blog);
        return BlogResponse.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .body(blog.getBody())
                .build();
    }

    public PageResponse<BlogResponse> findAllBlogs(int page, int size, Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("creationDate").descending());
        if(connectedUser == null) {
            return findAllBlogs(pageable);
        }
        User user = (User) connectedUser.getPrincipal();
        Page<Blog> blogs = blogRepository.findAllDisplayableBlogs(pageable, user.getId());
        List<BlogResponse> blogResponse = blogs.stream()
                .map(blogMapper::toBlogResponse)
                .toList();
        return new PageResponse<>(
                blogResponse,
                blogs.getNumber(),
                blogs.getSize(),
                blogs.getTotalElements(),
                blogs.getTotalPages(),
                blogs.isFirst(),
                blogs.isLast()
        );
    }

    private PageResponse<BlogResponse> findAllBlogs(Pageable pageable) {
        Page<Blog> blogs = blogRepository.findAll(pageable);
        List<BlogResponse> blogResponse = blogs.stream()
                .map(blogMapper::toBlogResponse)
                .toList();
        return new PageResponse<>(
                blogResponse,
                blogs.getNumber(),
                blogs.getSize(),
                blogs.getTotalElements(),
                blogs.getTotalPages(),
                blogs.isFirst(),
                blogs.isLast()
        );
    }

    public PageResponse<BlogResponse> findAllBlogsByOwner(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("creationDate").descending());
        Page<Blog> blogs = blogRepository.findAll(BlogSpecification.withOwnerId(user.getId()), pageable);
        List<BlogResponse> blogResponse = blogs.stream()
                .map(blogMapper::toBlogResponse)
                .toList();
        return new PageResponse<>(
                blogResponse,
                blogs.getNumber(),
                blogs.getSize(),
                blogs.getTotalElements(),
                blogs.getTotalPages(),
                blogs.isFirst(),
                blogs.isLast()
        );
    }

    public PageResponse<BlogResponse> findAllBlogsContainingTitle(int page, int size, String title) {
        Specification<Blog> specification = BlogSpecification.hasKeywordInTitle(title.trim());
        Pageable pageable = PageRequest.of(page, size, Sort.by("creationDate").descending());
        Page<Blog> blogs = blogRepository.findAll(specification, pageable);
        List<BlogResponse> blogResponse = blogs.stream()
                .map(blogMapper::toBlogResponse)
                .toList();
        return new PageResponse<>(
                blogResponse,
                blogs.getNumber(),
                blogs.getSize(),
                blogs.getTotalElements(),
                blogs.getTotalPages(),
                blogs.isFirst(),
                blogs.isLast()
        );
    }
}
