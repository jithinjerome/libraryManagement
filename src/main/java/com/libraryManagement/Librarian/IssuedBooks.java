package com.libraryManagement.Librarian;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "Issued_Books")
public class IssuedBooks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;


    @Column(name = "book_Id")
    private Long book_Id;

    @Column(name = "userId")
    private Long userId;

    @Column(name = "issue_Date")
    private LocalDate issueDate;

}
