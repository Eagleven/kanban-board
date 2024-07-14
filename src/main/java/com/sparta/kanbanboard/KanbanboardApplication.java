package com.sparta.kanbanboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KanbanboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(KanbanboardApplication.class, args);
    }

}
