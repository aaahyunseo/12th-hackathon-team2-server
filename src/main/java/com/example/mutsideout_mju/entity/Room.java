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
@Table(name = "rooms")
public class Room extends BaseEntity {
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String link;
    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Room setTitle(String title) {
        this.title = title;
        return this;
    }

    public Room setLink(String link) {
        this.link = link;
        return this;
    }

    public Room setContent(String content) {
        this.content = content;
        return this;
    }
}
