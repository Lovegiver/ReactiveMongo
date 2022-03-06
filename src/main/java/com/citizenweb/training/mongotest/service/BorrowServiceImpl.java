package com.citizenweb.training.mongotest.service;

import com.citizenweb.training.mongotest.model.Book;
import com.citizenweb.training.mongotest.model.Borrow;
import com.citizenweb.training.mongotest.model.User;
import com.mongodb.client.result.DeleteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Service
public class BorrowServiceImpl implements BorrowService {

    private final ReactiveMongoOperations mongoTemplate;

    @Autowired
    public BorrowServiceImpl(ReactiveMongoOperations mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Mono<Borrow> borrowBook(User user, Book book) {
        Book bookToBorrow = null;
        User borrowingUser = null;
        if (book.getId() == null) {
            Query query = new Query();
            query = query.addCriteria(Criteria.where("isbn").is(book.getIsbn()));
            bookToBorrow = mongoTemplate.findOne(query, Book.class).equals(Mono.empty())
                    ? mongoTemplate.save(Mono.just(book)).block()
                    : mongoTemplate.findOne(query, Book.class).block();
        } else {
            bookToBorrow = book;
        }
        if (user.getUserId() == null) {
            Query query = new Query();
            query = query.addCriteria(Criteria.where("firstName").is(user.getFirstName()));
            query = query.addCriteria(Criteria.where("lastName").is(user.getLastName()));
            borrowingUser = mongoTemplate.findOne(query, User.class).equals(Mono.empty())
                    ? mongoTemplate.save(Mono.just(user)).block()
                    : mongoTemplate.findOne(query, User.class).block();
            borrowingUser.setBorrows(new ArrayList<>());
        } else {
            borrowingUser = user;
            if (borrowingUser.getBorrows() == null) borrowingUser.setBorrows(new ArrayList<>());
        }
        Borrow borrow = Borrow.builder()
                .book(bookToBorrow)
                .user(borrowingUser)
                .build();
        borrowingUser.getBorrows().add(borrow);
        bookToBorrow.setBorrow(borrow);
        return mongoTemplate.save(borrow);
    }

    @Override
    public Mono<DeleteResult> returnBook(Borrow borrow) {
        return mongoTemplate.remove(borrow);
    }
}
