package com.example.mutsideout_mju.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "diaries")
public class Diary extends BaseEntity {
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    @Column
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Diary setTitle(String title) {
        this.title = title;
        return this;
    }

    public Diary setContent(String content) {
        this.content = content;
        return this;
    }

    public Diary setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
        return this;
    }
}
