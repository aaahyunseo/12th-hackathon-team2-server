package com.example.mutsideout_mju.entity;

import com.example.mutsideout_mju.exception.ForbiddenException;
import com.example.mutsideout_mju.exception.errorCode.ErrorCode;
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
    public Planner (User user, String content){
        this.user = user;
        this.content = content;
        this.isCompleted = false;
    }
    public void update(String content){
        validateComplete();
        this.content = content;
    }

    public void validateComplete(){
        if(isCompleted){
            throw new ForbiddenException(ErrorCode.INVALID_PLANNER_ACCESS, "완료된 플랜은 수정할 수 없습니다.");
        }
    }
    public void complete(){
        validateComplete();
        this.isCompleted = true;
    }
}
