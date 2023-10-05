package com.hari.cloud.app.service;

import com.hari.cloud.app.dao.Assignment;
import com.hari.cloud.app.dao.User;
import com.hari.cloud.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User getUserBy(String email) {
        if(userRepository.findById(email).isPresent()) {
            return userRepository.findById(email).get();
        }
        return null;
    }

    public User create(User user) {
        return userRepository.save(user);
    }
}
