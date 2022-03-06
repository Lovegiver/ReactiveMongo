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
    private String userId;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    @ReadOnlyProperty
    @DocumentReference(lookup="{'borrow':?#{#self._id} }")
    @Builder.Default
    @ToString.Exclude
    private List<Borrow> borrows = new ArrayList<>();
}
