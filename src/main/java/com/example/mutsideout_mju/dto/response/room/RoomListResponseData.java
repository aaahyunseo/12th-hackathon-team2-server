package com.example.mutsideout_mju.dto.response.room;

import com.example.mutsideout_mju.dto.response.PaginationData;
import com.example.mutsideout_mju.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class RoomListResponseData {
    private List<RoomResponseDto> roomList;
    private PaginationData pagination;
}
