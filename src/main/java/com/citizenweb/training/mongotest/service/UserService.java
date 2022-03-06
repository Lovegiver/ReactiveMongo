package com.citizenweb.training.mongotest.service;

import com.citizenweb.training.mongotest.model.User;
import com.citizenweb.training.mongotest.repository.UserRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<User> getUser(String userId);
    Flux<User> getAllUsers();
    Mono<User> saveUser(User user);
}
