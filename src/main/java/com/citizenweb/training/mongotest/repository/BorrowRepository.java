package com.citizenweb.training.mongotest.repository;

import com.citizenweb.training.mongotest.model.Borrow;
import com.citizenweb.training.mongotest.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface BorrowRepository extends ReactiveMongoRepository<Borrow,String> {
    Flux<Borrow> getBorrowsByUserOrderByUserIdAsc(Mono<User> userMono);
}
