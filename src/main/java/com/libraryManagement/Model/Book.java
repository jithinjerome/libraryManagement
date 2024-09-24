package com.libraryManagement.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private long id;
    private String title;
    private String author;

}