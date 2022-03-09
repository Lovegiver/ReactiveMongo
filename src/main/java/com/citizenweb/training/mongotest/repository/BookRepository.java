package com.citizenweb.training.mongotest.repository;

import com.citizenweb.training.mongotest.model.Book;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.Collection;
import java.util.List;

@Repository
public interface BookRepository extends ReactiveMongoRepository<Book,String> {

    Flux<Book> getAllByIdNotIn(Collection<String> id);
}
