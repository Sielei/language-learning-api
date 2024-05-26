package com.hs.languagelearningapi.lesson;

import com.hs.languagelearningapi.common.DTO;
import com.hs.languagelearningapi.lesson.UserProgressRepository;
import com.hs.languagelearningapi.user.UserProgressPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
class UserProgressAdapter implements UserProgressPort {
    private final UserProgressRepository userProgressRepository;

    UserProgressAdapter(UserProgressRepository userProgressRepository) {
        this.userProgressRepository = userProgressRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public DTO.PagedCollection<DTO.UserProgressDto> findUserProgress(UUID userId, int page, int pageSize) {
        var pageNo = page > 0 ? page - 1 : 0;
        var pageable = PageRequest.of(pageNo, pageSize);
        var userProgresssPage = userProgressRepository.findByUserId(userId, pageable)
                .map(userProgress -> new DTO.UserProgressDto(userProgress.getLesson().getLanguage().getName(),
                        userProgress.getLesson().getName(), userProgress.getAttemptedExercises(),
                        userProgress.getFailedExercises(), userProgress.getUserScore(),
                        userProgress.getTotalScore()));
        return new DTO.PagedCollection<>(
                userProgresssPage.getContent(),
                userProgresssPage.getTotalElements(),
                userProgresssPage.getNumber() + 1,
                userProgresssPage.getTotalPages(),
                userProgresssPage.isFirst(),
                userProgresssPage.isLast(),
                userProgresssPage.hasNext(),
                userProgresssPage.hasPrevious()
        );
    }
}
