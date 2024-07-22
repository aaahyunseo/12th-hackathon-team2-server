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
    private boolean isActive;

    public static RoomResponseData roomResponseData(Room room){
        return RoomResponseData.builder()
                .id(room.getId())
                .title(room.getTitle())
                .link(room.getLink())
                .content(room.getContent())
                .createdAt(room.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                //현재 시각으로 부터 방 생성 시간이 24시간 이전이면 true(활성화), 24시간 이후이면 false(비활성화)
                .isActive(room.getCreatedAt().isAfter(LocalDateTime.now().minusHours(24)))
                .build();
    }
}
