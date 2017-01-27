package com.data.postgres.repository;

import com.data.postgres.model.UserRaw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRawRepository extends JpaRepository<UserRaw, UUID> {
}
