package com.rukshesh.executor.api.repository;

import com.rukshesh.executor.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}
