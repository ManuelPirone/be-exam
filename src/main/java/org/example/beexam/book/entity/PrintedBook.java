package org.example.beexam.book.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "printed_books")
@Getter
@Setter
@NoArgsConstructor
public class PrintedBook extends Book {

    @Column(name = "physical_location", nullable = false)
    private String physicalLocation;

    @Column(nullable = false)
    private Integer pages;
}