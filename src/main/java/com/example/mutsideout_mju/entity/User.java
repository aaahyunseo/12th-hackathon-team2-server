package com.example.mutsideout_mju.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    private UserGrade userGrade;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserSurvey> userSurveys;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Planner> planners;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Diary> diaries;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> rooms;

    public void setUserGrade(UserGrade userGrade) {
        this.userGrade = userGrade;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserGrade determineGrade(long count) {
        if (count >= 4) {
            return UserGrade.TOMATO;
        } else if (count >= 2) {
            return UserGrade.BANANA;
        } else {
            return UserGrade.AVOCADO;
        }
    }
}
