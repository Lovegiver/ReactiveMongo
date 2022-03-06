package com.citizenweb.training.mongotest.repository;

import com.citizenweb.training.mongotest.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserRepository extends ReactiveMongoRepository<User,String> {
}
