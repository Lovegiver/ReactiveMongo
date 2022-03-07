package com.citizenweb.training.mongotest.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Document
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    @DocumentReference(lookup="{'user':?#{#self._id} }")
    @ToString.Exclude
    private List<Borrow> borrows = new ArrayList<>();
}
