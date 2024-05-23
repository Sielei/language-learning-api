package com.hs.languagelearningapi.user;

import com.hs.languagelearningapi.common.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

    default User findUserById(UUID userId){
        return findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id: " +userId + " does not exist"));
    }
}