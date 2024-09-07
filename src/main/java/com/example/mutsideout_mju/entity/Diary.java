package com.example.mutsideout_mju.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageFile> imageFiles;

    public Diary setTitle(String title) {
        this.title = title;
        return this;
    }

    public Diary setContent(String content) {
        this.content = content;
        return this;
    }

    public Diary(User user, String title, String content){
        this.user = user;
        this.title = title;
        this.content = content;
    }
}
