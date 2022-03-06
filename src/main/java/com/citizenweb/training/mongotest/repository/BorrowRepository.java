package com.citizenweb.training.mongotest.repository;

import com.citizenweb.training.mongotest.model.Borrow;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface BorrowRepository extends ReactiveMongoRepository<Borrow,String> {
}
