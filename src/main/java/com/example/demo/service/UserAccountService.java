package com.example.demo.service;

import java.util.List;
import com.example.demo.entity.UserAccount;

public interface UserAccountService {

    UserAccount saveUser(UserAccount userAccount);

    List<UserAccount> getAllUsers();

    UserAccount getUserById(Long id);
}
