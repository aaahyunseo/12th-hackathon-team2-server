package com.example.mutsideout_mju.dto.response.user;

import com.example.mutsideout_mju.dto.response.planner.DailyPlannerCompletionDataList;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyPageResponseData {
    private ProfileResponseData profileResponseData;

    public static MyPageResponseData of(ProfileResponseData profileResponseData) {
        return builder()
                .profileResponseData(profileResponseData).build();
    }
}

