package com.galvanize.services;

import com.galvanize.entities.User;
import com.galvanize.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    UserRepo userRepo;

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User login(String email, String password) {
        User user = userRepo.findByEmailAndPassword(email, password);
        if(user != null) {
            return user;
        }else {
            throw new RuntimeException("User password combination not found");
        }
    }

    public int logout(User user) {
        if(user != null && user.getId() != null) {
            return 200;
        }else{
            return 400;
        }
    }

    public User register(User user) {
        if(userRepo.findByEmail(user.getEmail()).isPresent()){
            throw new RuntimeException("Email already exists");
        }
        if(user.getPassword().equals(user.getRepeatPassword())) {
            return userRepo.save(user);
        }else{
            throw new RuntimeException("Passwords do not match");
        }
    }

    public boolean emailExists(String email) {
        return userRepo.findByEmail(email).isPresent();
    }
}
