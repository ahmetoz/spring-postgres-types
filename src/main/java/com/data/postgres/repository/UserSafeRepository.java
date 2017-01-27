package com.data.postgres.repository;

import com.data.postgres.model.UserSafe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserSafeRepository extends JpaRepository<UserSafe, UUID> {
}
