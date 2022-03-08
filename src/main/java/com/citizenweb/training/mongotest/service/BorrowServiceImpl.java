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

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Log
@Service
public class BorrowServiceImpl implements BorrowService {

    private final ReactiveMongoOperations mongoTemplate;
    private final String MONGO_COLLECTION = "borrow";

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
        savedBorrowMono.subscribe(savedBorrow -> {
            mongoTemplate.update(User.class)
                    .matching(where("id").is(user.getId()))
                    .apply(new Update().push("borrows", savedBorrow))
                    .first();
            mongoTemplate.update(Book.class)
                    .matching(where("id").is(book.getId()))
                    .apply(new Update().push("borrow", savedBorrow))
                    .first();
        });
        return savedBorrowMono;
    }

    @Override
    public Mono<Borrow> borrowBook(Mono<User> userMono, Mono<Book> bookMono) {
        User argUser = userMono.block();
        Book argBook = bookMono.block();
        Borrow borrow = Borrow.builder()
                .book(argBook)
                .user(argUser)
                .build();
        Mono<Borrow> savedBorrowMono = mongoTemplate.save(borrow);
        Borrow savedBorrow = savedBorrowMono.block();
        mongoTemplate.update(User.class)
                .matching(where("_id").is(argUser.getId()))
                .apply(new Update().push("borrows",savedBorrow))
                .first();
        mongoTemplate.update(Book.class)
                .matching(where("_id").is(argBook.getId()))
                .apply(new Update().push("borrow",savedBorrow))
                .first();
        return savedBorrowMono;
    }

    @Override
    public Mono<DeleteResult> returnBook(Borrow borrow) {
        return mongoTemplate.remove(borrow, MONGO_COLLECTION);
    }

    @Override
    public Mono<DeleteResult> returnBook(Mono<Borrow> borrow) {
        return mongoTemplate.remove(borrow, MONGO_COLLECTION);
    }
}
