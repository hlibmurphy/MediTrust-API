package com.github.edocapi.repository;

import com.github.edocapi.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.phone = :phone")
    Optional<User> findByPhoneWithRoles(String phone);

    Optional<User> findByPhone(String phone);
}
