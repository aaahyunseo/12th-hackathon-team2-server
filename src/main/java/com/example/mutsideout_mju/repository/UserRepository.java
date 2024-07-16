package com.example.mutsideout_mju.repository;

import com.example.mutsideout_mju.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

}
