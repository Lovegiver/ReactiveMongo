package com.citizenweb.training.mongotest;

import com.citizenweb.training.mongotest.model.Book;
import com.citizenweb.training.mongotest.model.Borrow;
import com.citizenweb.training.mongotest.model.User;
import com.citizenweb.training.mongotest.repository.BookRepository;
import com.citizenweb.training.mongotest.repository.BorrowRepository;
import com.citizenweb.training.mongotest.repository.UserRepository;
import com.citizenweb.training.mongotest.service.BorrowService;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Log
@SpringBootTest
class MongoTestApplicationTests {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BorrowRepository borrowRepository;
    @Autowired
    private BorrowService borrowService;
    @Autowired
    private ReactiveMongoOperations mongoTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void createMassiveUser() throws InterruptedException {

        long startTime = System.currentTimeMillis();
        int count = 1000;
        Flux<User> userFlux = Flux.empty();

        for (int i=0; i<count; i++) {
            User user = User.builder()
                    .firstName("firstname"+i)
                    .lastName("lastname"+i)
                    .birthDate(LocalDate.now().plus(i, ChronoUnit.DAYS))
                    .build();
            Flux<User> savedUser = userRepository.save(user).flux();
            userFlux = Flux.concat(userFlux,savedUser);
        }

        userFlux.subscribe(user -> log.info(user.getFirstName()));

        Thread.sleep(2000);
        log.info(String.format("Elapsed time = [ %d ] millis", System.currentTimeMillis()-startTime));

    }

    @Test
    void borrowBook() {

        User user = User.builder().firstName("Fred").lastName("Courcier").build();
        User savedUser = userRepository.save(user).block();
        Assertions.assertNotNull(savedUser);
        Book book = Book.builder().isbn("isbn1").author("Jules Verne").title("Blabla").build();
        Book savedBook = bookRepository.save(book).block();
        Assertions.assertNotNull(savedBook);

        Borrow savedBorrow = borrowService.borrowBook(savedUser,savedBook).block();
        Assertions.assertNotNull(savedBorrow);
        log.info(savedBorrow.toString());

        User userFromDB = mongoTemplate.findById(savedUser.getId(), User.class).block();
        log.info(String.format("User -> %s & Borrow [ %s ]", userFromDB, userFromDB.getBorrows().get(0).getId()));
        Book bookFromDB = mongoTemplate.findById(savedBook.getId(), Book.class).block();
        log.info(String.format("Book -> %s & Borrow [ %s ]", bookFromDB, bookFromDB.getBorrow().getId()));

    }

}
