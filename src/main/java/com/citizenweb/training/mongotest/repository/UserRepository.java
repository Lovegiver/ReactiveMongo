package com.citizenweb.training.mongotest.repository;

import com.citizenweb.training.mongotest.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User,String> {
}
