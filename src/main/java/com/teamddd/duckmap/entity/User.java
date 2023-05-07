package com.teamddd.duckmap.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity @Getter
@Table(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;
    private String salt;
    private String image;
    private LocalDateTime createdAt;
    private LocalDateTime loginAt;

}
