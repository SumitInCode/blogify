package com.ssuamkiett.blogify.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@EntityListeners(AuditingEntityListener.class)
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String title;
    @Column(columnDefinition = "LONGTEXT")
    private String body;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime creationDate;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;
    String imageFilePath;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    @JsonIgnore
    private User owner;
    private String authorName;
    @OneToMany(mappedBy = "blog")
    private List<Feedback> feedbacks;
    private String tags;
    @Transient
    public double getRate() {
        if(feedbacks == null || feedbacks.isEmpty()) {
            return 0.0;
        }
        double rate = this.feedbacks.stream()
                .mapToDouble(Feedback::getNote)
                .average()
                .orElse(0.0);
        return Math.round(rate * 10.0) / 10.0;
    }
}
