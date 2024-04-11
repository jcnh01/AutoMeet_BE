package com.AutoMeet.domain.user.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User {

    @Id @GeneratedValue
    private Long id;

    private String email;

    private String password;

    private String name;

    private Integer age;

    @Builder
    public User(String email, String password, String name, Integer age) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.age = age;
    }
}
