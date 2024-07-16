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
@Table(name = "surveys")
public class Survey extends BaseEntity{

    @Column(nullable = false)
    private Long number;
    @Column(nullable = false)
    private String question;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserSurvey> userSurveys;
}
