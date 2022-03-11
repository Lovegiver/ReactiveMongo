package com.citizenweb.training.mongotest.model;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Book {

    @Getter @Setter
    @Id
    private String id;
    @Getter @Setter
    private String title;
    @Getter @Setter
    private String author;
    @Getter @Setter
    private String isbn;
    @Getter @Setter
    @DocumentReference(lookup="{'book':?#{#self._id} }")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Borrow borrow;
}
