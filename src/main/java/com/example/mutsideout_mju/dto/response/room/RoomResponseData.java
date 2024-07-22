package com.example.mutsideout_mju.dto.response.room;

import com.example.mutsideout_mju.entity.Room;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@Builder
public class RoomResponseData {
    private UUID id;
    private String title;
    private String link;
    private String content;
    private String createdAt;

    public static RoomResponseData roomResponseData(Room room){
        return RoomResponseData.builder()
                .id(room.getId())
                .title(room.getTitle())
                .link(room.getLink())
                .content(room.getContent())
                .createdAt(room.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .build();
    }
}
