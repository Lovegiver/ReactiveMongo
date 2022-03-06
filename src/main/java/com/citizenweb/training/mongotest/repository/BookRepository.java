package com.citizenweb.training.mongotest.repository;

import com.citizenweb.training.mongotest.model.Book;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BookRepository extends ReactiveMongoRepository<Book,String> {
}
