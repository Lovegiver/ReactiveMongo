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
public class Borrow {

    @Id
    @Getter @Setter
    private String id;
    @Getter @Setter
    @DocumentReference
    private Book book;
    @Getter @Setter
    @DocumentReference
    private User user;
}
