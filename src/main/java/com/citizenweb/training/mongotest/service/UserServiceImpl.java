package com.citizenweb.training.mongotest.service;

import com.citizenweb.training.mongotest.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

    private final ReactiveMongoOperations mongoTemplate;

    @Autowired
    public UserServiceImpl(ReactiveMongoOperations mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Mono<User> getUser(String userId) {
        return mongoTemplate.findById(userId, User.class);
    }

    @Override
    public Flux<User> getAllUsers() {
        return mongoTemplate.findAll(User.class);
    }

    @Override
    public Mono<User> saveUser(User user) {
        return mongoTemplate.save(user);
    }
}
