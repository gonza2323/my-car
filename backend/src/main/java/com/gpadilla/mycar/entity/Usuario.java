package com.gpadilla.mycar.entity;

import com.gpadilla.mycar.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario extends BaseEntity<Long> {

    @Column(nullable = false)
    private String email;

    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole rol;

    private String providerId;

    @Column(nullable = false)
    private Boolean hasCompletedProfile;

    @Column(nullable = false)
    private Boolean mustChangePassword;
}
