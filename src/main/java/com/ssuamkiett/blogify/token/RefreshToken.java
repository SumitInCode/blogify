package com.ssuamkiett.blogify.token;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RefreshToken {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(nullable = false, unique = true)
    private String token;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime expiresAt;
}
