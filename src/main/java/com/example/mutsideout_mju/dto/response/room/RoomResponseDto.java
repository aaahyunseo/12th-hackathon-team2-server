package com.example.mutsideout_mju.dto.response.room;

import com.example.mutsideout_mju.entity.Room;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@Builder
public class RoomResponseDto {
    private UUID id;
    private String title;
    private String createdAt;
    private String imageLink;

    public static RoomResponseDto fromRoom(Room room, String imageLink){
        return RoomResponseDto.builder()
                .id(room.getId())
                .title(room.getTitle())
                .createdAt(room.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .imageLink(imageLink)
                .build();
    }
}
