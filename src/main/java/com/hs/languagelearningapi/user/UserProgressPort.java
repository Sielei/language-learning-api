package com.hs.languagelearningapi.user;

import com.hs.languagelearningapi.common.DTO;

import java.util.UUID;

public interface UserProgressPort {
    DTO.PagedCollection<DTO.UserProgressDto> findUserProgress(UUID userId, int page, int pageSize);
}
