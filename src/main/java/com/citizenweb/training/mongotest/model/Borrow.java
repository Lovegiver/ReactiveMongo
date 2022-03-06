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
public class Borrow {

    @Id
    private String id;
    @DocumentReference
    private Book book;
    @DocumentReference
    private User user;
}
