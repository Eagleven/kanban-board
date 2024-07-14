package com.sparta.kanbanboard.domain.user.service;

import com.sparta.kanbanboard.domain.user.repository.DeletedUserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CleanupService {

    private final DeletedUserRepository deletedUserRepository;

    // Set the retention period, e.g., 30 days
    private static final int RETENTION_PERIOD_DAYS = 30;

    @Scheduled(cron = "0 0 0 * * ?") // Run once daily at midnight
    public void cleanupDeletedUsers() {
        LocalDateTime expirationTime = LocalDateTime.now().minusDays(RETENTION_PERIOD_DAYS);
        var expiredUsers = deletedUserRepository.findExpiredUsers(expirationTime);
        
        if (!expiredUsers.isEmpty()) {
            log.info("Deleting {} expired deleted users", expiredUsers.size());
            deletedUserRepository.deleteAll(expiredUsers);
        } else {
            log.info("No expired deleted users found to delete");
        }
    }
}