package com.project.sideproject.service;

import com.project.sideproject.models.User;
import com.project.sideproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUser(String username){
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public void save(User user){
        userRepository.save(user);
    }

    public void delete(User user){
        userRepository.delete(user);
    }

    public boolean isAuthenticated() {
        return getAuthentication().isAuthenticated() && getAuthentication().getPrincipal() instanceof UserDetails;
    }

    public boolean isAuthorized(){
        return isAuthenticated() && ((UserDetails) getAuthentication().getPrincipal()).getAuthorities().toString().contains("ROLE_ADMIN");

    }

    public Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public Object getPrincipal(){
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public String getUsername(){
        if(getPrincipal() instanceof UserDetails) {
            return ((UserDetails) getPrincipal()).getUsername();
        }
        return null;
    }
}
