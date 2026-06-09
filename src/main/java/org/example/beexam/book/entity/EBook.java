package org.example.beexam.book.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ebooks")
@Getter
@Setter
@NoArgsConstructor
public class EBook extends Book {

    @Column(name = "file_format", nullable = false)
    private String fileFormat;

    @Column(name = "file_url", nullable = false)
    private String fileUrl;
}