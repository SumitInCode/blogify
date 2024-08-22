package com.ssuamkiett.blogify.models;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Feedback {
    @Id
    @GeneratedValue
    private Long id;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime creationDate;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;
    @CreatedBy
    @Column(nullable = false, updatable = false)
    private Long createdBy;
    @LastModifiedBy
    @Column(insertable = false)
    private Integer lastModifiedBy;
    private Double note; // 1-5 stars
    private String comment;
    @ManyToOne
    @JoinColumn(name = "blog_id")
    private Blog blog;
}
