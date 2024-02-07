package com.example.stock.repositories;

import com.example.stock.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, String> {

    UserDetails findByLogin(String login);

    User findUserRoleByLogin(String login);

}
