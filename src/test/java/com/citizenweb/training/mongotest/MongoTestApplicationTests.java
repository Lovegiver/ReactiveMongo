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
import reactor.core.publisher.Mono;

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

    //@Test
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
        final String MONGO_COLLECTION = "borrowings";
        User user = User.builder()
                .firstName("Fred")
                .lastName("Courcier")
                .birthDate(LocalDate.of(1971,7,8))
                .build();
        //Mono<User> u = mongoTemplate.save(user,MONGO_COLLECTION);
        Mono<User> u = userRepository.save(user);
        Assertions.assertNotNull(u);
        Book book = Book.builder()
                .isbn("isbn1")
                .author("Jules Verne")
                .title("Blabla")
                .build();
        //Mono<Book> b = mongoTemplate.save(book, MONGO_COLLECTION);
        Mono<Book> b = bookRepository.save(book);
        Assertions.assertNotNull(b);

        Mono<Borrow> brw = mongoTemplate.save(borrowService.borrowBook(u,b), MONGO_COLLECTION);
        Assertions.assertNotNull(brw);
        brw.subscribe(borrow -> log.info(String.format("Saved borrowing -> [ %s ]", borrow)));

    }

}
