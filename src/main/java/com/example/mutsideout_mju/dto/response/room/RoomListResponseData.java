package com.example.mutsideout_mju.dto.response.room;

import com.example.mutsideout_mju.dto.response.PaginationData;
import com.example.mutsideout_mju.entity.Room;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class RoomListResponseData {
    private List<RoomResponseDto> roomList;
    private PaginationData pagination;

    public static RoomListResponseData roomListResponseData(Page<Room> page){
        return RoomListResponseData.builder()
                .roomList(page.stream()
                        .map(room -> RoomResponseDto.roomResponseDto(room))
                        .collect(Collectors.toList()))
                .pagination(PaginationData.paginationData(page))
                .build();
    }
}
