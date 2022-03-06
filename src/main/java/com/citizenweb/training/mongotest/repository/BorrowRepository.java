package com.citizenweb.training.mongotest.repository;

import com.citizenweb.training.mongotest.model.Borrow;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface BorrowRepository extends ReactiveMongoRepository<Borrow,String> {
}
