package com.citizenweb.training.mongotest.service;

import com.citizenweb.training.mongotest.model.Book;
import com.citizenweb.training.mongotest.model.Borrow;
import com.citizenweb.training.mongotest.model.User;
import com.citizenweb.training.mongotest.repository.BorrowRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class BorrowServiceImpl implements BorrowService {

    private final BorrowRepository borrowRepository;
    private final BookService bookService;
    private final UserService userService;

    public BorrowServiceImpl(BorrowRepository borrowRepository, BookService bookService, UserService userService) {
        this.borrowRepository = borrowRepository;
        this.bookService = bookService;
        this.userService = userService;
    }

    @Override
    public Mono<Borrow> borrowBook(User user, Book book) {
        Book bookToBorrow;
        User borrowingUser;
        if (book.getId() == null) {
            bookToBorrow = bookService.saveBook(book).block();
        } else {
            bookToBorrow = book;
        }
        if (user.getUserId() == null) {
            borrowingUser = userService.saveUser(user).block();
        } else {
            borrowingUser = user;
        }
        Borrow borrow = Borrow.builder()
                .book(book)
                .user(user)
                .build();
        user.getBorrows().add(borrow);
        book.setBorrow(borrow);
        return borrowRepository.save(borrow);
    }

    @Override
    public Mono<Void> returnBook(Borrow borrow) {
        return borrowRepository.delete(borrow);
    }
}
