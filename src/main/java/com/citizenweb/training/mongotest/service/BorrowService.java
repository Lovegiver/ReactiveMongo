package com.citizenweb.training.mongotest.service;

import com.citizenweb.training.mongotest.model.Book;
import com.citizenweb.training.mongotest.model.Borrow;
import com.citizenweb.training.mongotest.model.User;
import com.mongodb.client.result.DeleteResult;
import reactor.core.publisher.Mono;

public interface BorrowService {
    Mono<Borrow> borrowBook(User user, Book book);
    Mono<Borrow> borrowBook(Mono<User> userMono, Mono<Book> bookMono);
    Mono<DeleteResult> returnBook(Borrow borrow);
    Mono<DeleteResult> returnBook(Mono<Borrow> borrow);
}
