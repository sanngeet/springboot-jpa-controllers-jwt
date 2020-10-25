package com.example.authentication.controller;

import com.example.authentication.dto.UserDto;
import com.example.authentication.entity.User;
import com.example.authentication.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@RequestBody User user) {
        if (user.getPassword().length() < 8 || !user.getPassword().equals(user.getConfirmPassword())) {
            return ResponseEntity.badRequest().build();
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getUser(@PathVariable String username) {
        User user = userRepository.findByUsernameContainingIgnoreCase(username);
        if (user != null) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto);
            return ResponseEntity.ok(userDto);
        }

        return ResponseEntity.notFound().build();
    }

}
