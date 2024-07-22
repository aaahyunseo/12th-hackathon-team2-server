package com.example.mutsideout_mju.dto.response.room;

import com.example.mutsideout_mju.entity.Room;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@Builder
public class RoomResponseDto {
    private UUID id;
    private String title;
    private String createdAt;
    private boolean isActive;

    public static RoomResponseDto roomResponseDto(Room room){
        return RoomResponseDto.builder()
                .id(room.getId())
                .title(room.getTitle())
                .createdAt(room.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                //현재 시각으로 부터 방 생성 시간이 24시간 이전이면 true(활성화), 24시간 이후이면 false(비활성화)
                .isActive(room.getCreatedAt().isAfter(LocalDateTime.now().minusHours(24)))
                .build();
    }
}
