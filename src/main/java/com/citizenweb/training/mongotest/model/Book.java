package com.citizenweb.training.mongotest.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Book {

    @Id
    private String id;
    private String title;
    private String author;
    private String isbn;
    @DocumentReference(lookup="{'book':?#{#self._id} }")
    @ToString.Exclude
    private Borrow borrow;
}
