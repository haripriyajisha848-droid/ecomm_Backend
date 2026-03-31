package com.example.ecommapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    private String email;

    private String password;   // will be stored as bcrypt hash, never plain text

    private String firstName;

    private String lastName;

    private String phone;

    private List<String> roles;   // ["ROLE_CUSTOMER"] or ["ROLE_ADMIN"]

    private LocalDateTime createdAt;
}