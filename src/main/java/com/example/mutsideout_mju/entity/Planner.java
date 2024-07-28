package com.example.mutsideout_mju.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "planners")
public class Planner extends BaseEntity {

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean isCompleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void setCompleted(boolean a){
        isCompleted = a;
    }
    public void setContent(String content){
        this.content = content;
    }

    public static StatsGrade determineStatsGrade(long count) {
        if (count >= 4) {
            return StatsGrade.LEVEL3;
        } else if (count >= 2) {
            return StatsGrade.LEVEL2;
        } else {
            return StatsGrade.LEVEL1;
        }
    }
}
