package com.citizenweb.training.mongotest.model;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Document
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class User {

    @Getter @Setter
    @Id
    private ObjectId id;
    @Getter @Setter
    private String firstName;
    @Getter @Setter
    private String lastName;
    @Getter @Setter
    private LocalDate birthDate;
    @Getter @Setter
    @DocumentReference(lookup="{'user':?#{#self._id} }")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Borrow> borrows = new ArrayList<>();
}
