package com.example.mutsideout_mju.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class RefreshToken {
    UUID userId;
    @Id
    UUID tokenId;
}
