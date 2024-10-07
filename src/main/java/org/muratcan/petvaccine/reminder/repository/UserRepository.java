package org.muratcan.petvaccine.reminder.repository;

import org.muratcan.petvaccine.reminder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findById(String id);

    Optional<User> findByEmail(String email);
}

