package com.site.firstsite.repository;

import com.site.firstsite.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
    User findById(long id);
    List<User> findByEmail(String email);
    List<User> findByStatus(boolean status);
    List<User> findByUsernameContaining(String username);
    List<User> findByStatusAndStatusTimestampAfter(boolean status, Timestamp timestamp);
    List<User> findByStatusTimestampAfter(Timestamp timestamp);
}