package com.example.mutsideout_mju;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MutsideoutMjuApplication {

    public static void main(String[] args) {
        SpringApplication.run(MutsideoutMjuApplication.class, args);
    }

}
