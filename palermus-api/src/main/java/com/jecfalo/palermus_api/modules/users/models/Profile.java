package com.jecfalo.palermus_api.modules.users.models;

import com.jecfalo.palermus_api.core.security.AttributeEncryptor;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "profiles")
@EntityListeners(AuditingEntityListener.class)
public class Profile {
    @Id
    private Long id;
    @Column(unique = true)
    @Convert(converter = AttributeEncryptor.class)
    private String document;
    private String names;
    private String surnames;
    @Column(unique = true)
    @Convert(converter = AttributeEncryptor.class)
    private String email;
    @Enumerated(EnumType.STRING)
    private UserType userType;
    @CreatedDate
    @Column(updatable = false)
    private LocalDate createdAt;
    @LastModifiedDate
    private LocalDate updateAt;
    @OneToOne @MapsId @JoinColumn(name = "userId")
    private User user;
    private boolean profileActive;
}
