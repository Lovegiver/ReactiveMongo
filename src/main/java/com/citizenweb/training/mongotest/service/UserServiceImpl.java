package com.citizenweb.training.mongotest.service;

import com.citizenweb.training.mongotest.model.User;
import com.citizenweb.training.mongotest.repository.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<User> getUser(String userId) {
        return userRepository.findById(Mono.just(userId));
    }

    @Override
    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Mono<User> saveUser(User user) {
        return userRepository.save(user);
    }
}
