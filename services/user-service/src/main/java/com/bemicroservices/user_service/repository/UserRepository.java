package com.bemicroservices.user_service.repository;

import com.bemicroservices.user_service.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
