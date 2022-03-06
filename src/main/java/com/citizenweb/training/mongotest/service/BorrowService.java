package com.citizenweb.training.mongotest.service;

import com.citizenweb.training.mongotest.model.Book;
import com.citizenweb.training.mongotest.model.Borrow;
import com.citizenweb.training.mongotest.model.User;
import reactor.core.publisher.Mono;

public interface BorrowService {
    Mono<Borrow> borrowBook(User userMono, Book bookMono);
    Mono<Void> returnBook(Borrow borrow);
}
