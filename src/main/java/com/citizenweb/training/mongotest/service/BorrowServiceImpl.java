package com.citizenweb.training.mongotest.service;

import com.citizenweb.training.mongotest.model.Book;
import com.citizenweb.training.mongotest.model.Borrow;
import com.citizenweb.training.mongotest.model.User;
import com.mongodb.client.result.DeleteResult;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Log
@Service
public class BorrowServiceImpl implements BorrowService {

    private final ReactiveMongoOperations mongoTemplate;

    @Autowired
    public BorrowServiceImpl(ReactiveMongoOperations mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Mono<Borrow> borrowBook(User user, Book book) {

        Borrow borrow = Borrow.builder()
                .book(book)
                .user(user)
                .build();

        Mono<Borrow> savedBorrowMono = mongoTemplate.save(borrow);
        while (savedBorrowMono.block(Duration.ofMillis(1000)) == null) {
            log.info("Waiting for reactive app...");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        savedBorrowMono.subscribe(savedBorrow -> {
            mongoTemplate.update(User.class)
                    .matching(where("id").is(user.getId()))
                    .apply(new Update().push("borrows").value(Objects.requireNonNull(savedBorrow)))
                    .first();
            mongoTemplate.update(Book.class)
                    .matching(where("id").is(book.getId()))
                    .apply(new Update().push("borrow").value(Objects.requireNonNull(savedBorrow)))
                    .first();
        });
        return savedBorrowMono;
    }

    @Override
    public Mono<DeleteResult> returnBook(Borrow borrow) {
        return mongoTemplate.remove(borrow);
    }
}
