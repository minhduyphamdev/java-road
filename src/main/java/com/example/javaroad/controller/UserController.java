package com.example.javaroad.controller;

import com.example.javaroad.entity.User;
import com.example.javaroad.exception.ResourceNotFoundException;
import com.example.javaroad.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public List<User> getAllUsers()
    {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value="id") Long id) throws ResourceNotFoundException {
        User user = userRepository.findById(id).orElseThrow(()->{
            return new ResourceNotFoundException("Cannot find user with id="+id);
        });
        return ResponseEntity.ok().body(user);
    }
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user)
    {
        User newUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable(value="id",required = true) Long id,@Valid @RequestBody User user) throws ResourceNotFoundException
    {
        User newUser = userRepository.findById(id).orElseThrow(()-> {
            return new ResourceNotFoundException("Cannot find user with id=" + id);
        });
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setEmailId(user.getEmailId());
        newUser.setLastModifiedDate(new Date());

        final User savedUser = userRepository.save(newUser);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(savedUser);
    }

    @DeleteMapping("/users/{id}")
    public Map<String,Boolean> deleteUser(@PathVariable(value="id") Long id) throws ResourceNotFoundException
    {
        User user = userRepository.findById(id).orElseThrow(()-> {
            return new ResourceNotFoundException("Cannot find User with id="+id);
        });
        userRepository.delete(user);
        Map<String ,Boolean> response = new HashMap<>();
        response.put("deleted",Boolean.TRUE);
        return response;
    }


}
