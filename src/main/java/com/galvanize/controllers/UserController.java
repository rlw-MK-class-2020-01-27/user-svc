package com.galvanize.controllers;

import com.galvanize.entities.LoginRequest;
import com.galvanize.entities.LogoutRequest;
import com.galvanize.entities.User;
import com.galvanize.services.UserService;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<User> postLogin(@RequestBody LoginRequest login){
        User user;
        try {
            user = userService.login(login.getEmail(), login.getPassword());
            return ResponseEntity.ok(user);
        }catch (RuntimeException re){
            return ResponseEntity.badRequest().build();
        }

    }

    @PostMapping("/logout")
    public ResponseEntity postLogout(@RequestBody LogoutRequest logout){
        if(userService.emailExists(logout.getEmail())){
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<User> postRegister(@RequestBody User user, HttpServletResponse response, HttpServletRequest request){
        try {
            String msg = request.getHeader("clientMsg");
            user = userService.register(user);
        }catch (RuntimeException re){
            response.addHeader("errorMessage", re.getMessage());
            return ResponseEntity.badRequest().build();
        }
        if (user != null && user.getId() != null){
            return ResponseEntity.ok(user);
        }else{
            response.addHeader("errorMessage", "It didn't work");
            return ResponseEntity.badRequest().build();
        }
    }


}
