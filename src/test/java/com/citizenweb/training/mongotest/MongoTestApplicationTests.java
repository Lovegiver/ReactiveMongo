package com.citizenweb.training.mongotest;

import com.citizenweb.training.mongotest.model.Book;
import com.citizenweb.training.mongotest.model.Borrow;
import com.citizenweb.training.mongotest.model.User;
import com.citizenweb.training.mongotest.repository.BookRepository;
import com.citizenweb.training.mongotest.repository.BorrowRepository;
import com.citizenweb.training.mongotest.repository.UserRepository;
import com.citizenweb.training.mongotest.service.BookService;
import com.citizenweb.training.mongotest.service.BorrowService;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private BookService bookService;

    private final Book BOOK_1 = Book.builder()
            .isbn("isbn1")
            .author("Jules Verne")
            .title("Vingt-mille lieues sous les mers")
            .build();
    private final Book BOOK_2 = Book.builder()
            .isbn("isbn2")
            .author("René Girard")
            .title("La violence et le Sacré")
            .build();
    private final Book BOOK_3 = Book.builder()
            .isbn("isbn3")
            .author("David Graeber")
            .title("Bullshit jobs")
            .build();
    private final User USER_1 = User.builder().firstName("Tom").lastName("Reilly")
            .birthDate(LocalDate.of(1987,8,2)).build();
    private final User USER_2 = User.builder().firstName("Jessica").lastName("Lange")
            .birthDate(LocalDate.of(1992,4,22)).build();
    private final User USER_3 = User.builder().firstName("Mary").lastName("Predator")
            .birthDate(LocalDate.of(1947,1,14)).build();

    @BeforeEach
    void prepareTest() {
        cleanDB();
    }

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
        Mono<User> u = userRepository.save(USER_1);
        Assertions.assertNotNull(u);
        Mono<Book> b = bookRepository.save(BOOK_1);
        Assertions.assertNotNull(b);

        Mono<Borrow> brw = mongoTemplate.save(borrowService.borrowBook(u,b));
        Assertions.assertNotNull(brw);
        log.info(String.format("Saved borrowing -> [ %s ]", brw.block()));

    }

    @Test
    void borrowsByUser() {
        Mono<User> userMono = mongoTemplate.findById("62268cce6a68c3473c3cd87b",User.class);
        log.info(String.format("Found User [ %s ]", userMono.block()));
        borrowRepository.getBorrowsByUserOrderByUserIdAsc(userMono)
                .log()
                .subscribe(brw -> log.info("BORROW : " + brw.getId()));
    }

    @Test
    void queryCrossRepositories() {
        Mono<Book> bookMono = bookService.saveBook(Book.builder()
                        .title("La violence et le Sacré")
                        .author("René Girard")
                        .isbn("isbn1971")
                        .build());
        Assertions.assertNotNull(bookMono);
        log.info("Saved book : " + bookMono.block());
        List<Book> borrowedBooks = new ArrayList<>();
        mongoTemplate.findAll(Borrow.class)
                .toIterable()
                .forEach(i -> {
                    borrowedBooks.add(i.getBook());
                });
        log.info(String.format("List of borrowed books contains %s elements", borrowedBooks.size()));
        Query query = new Query(Criteria.where("_id").not().in(borrowedBooks));
        Flux.from(mongoTemplate.find(query, Book.class)).retry()
                .log()
                .subscribe(book -> {
                    log.info("Book still on shelves : " + book);
                });

    }

    @Test
    void getNotBorrowedBooks() {
        Query query = new Query();
        query.addCriteria(Criteria.where("author").is("René Girard"));
        List<Book> borrowedBooks = new ArrayList<>();
        mongoTemplate.find(query, Book.class)
                .toIterable()
                .forEach(borrowedBooks::add);
        Assert.notEmpty(borrowedBooks, "No books in DB)");
        log.info(String.format("Found [ %s ]", borrowedBooks.get(0).getTitle()));

        Flux<Book> notBorrowedBooks = bookRepository
                .getAllByIdNotIn(Collections.singletonList(borrowedBooks.get(0).getId()));
        notBorrowedBooks
                .log()
                .subscribe(book -> {
                    log.info(String.format("Not borrowed : [ %s ]", book.getId()));
                });
    }

    /*List<ObjectId> restaurantList = mongoTemplate.findDistinct(new Query(Criteria
            .where("address.location")
            .withinSphere(new Circle(latitude, longitude, radius / 6371))),"cocktailList",Restaurant.class,ObjectId.class);
    return cocktailRepository.findBy_idIn(new HashSet<>(restaurantList));*/

    private void cleanDB() {
        log.info("Cleaning Database");
        bookRepository.deleteAll();
        userRepository.deleteAll();
        bookRepository.deleteAll();
    }

    private <T> Flux<T> createObjectsInDB(List<T> objectsToInsert, Class<T> clazz) {
        return mongoTemplate.insert(objectsToInsert,clazz);
    }

}
