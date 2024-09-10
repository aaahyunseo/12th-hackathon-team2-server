package com.example.mutsideout_mju.dto.response.room;

import com.example.mutsideout_mju.dto.response.PaginationData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class RoomListResponseData {
    private List<RoomResponseDto> roomList;
    private PaginationData pagination;
}
