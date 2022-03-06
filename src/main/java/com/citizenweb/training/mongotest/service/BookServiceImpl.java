package com.citizenweb.training.mongotest.service;

import com.citizenweb.training.mongotest.model.Book;
import com.citizenweb.training.mongotest.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BookServiceImpl implements BookService {

    private final ReactiveMongoOperations mongoTemplate;

    @Autowired
    public BookServiceImpl(ReactiveMongoOperations mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Mono<Book> getBook(String bookId) {
        return mongoTemplate.findById(bookId, Book.class);
    }

    @Override
    public Flux<Book> getAllBooks() {
        return mongoTemplate.findAll(Book.class);
    }

    @Override
    public Mono<Book> saveBook(Book book) {
        return mongoTemplate.save(book);
    }
}
