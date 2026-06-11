package org.example.beexam.book.service;

import lombok.RequiredArgsConstructor;
import org.example.beexam.book.dto.AuthorRequest;
import org.example.beexam.book.dto.AuthorResponse;
import org.example.beexam.book.entity.Author;
import org.example.beexam.book.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Transactional(readOnly = true)
    public List<AuthorResponse> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public AuthorResponse createAuthor(AuthorRequest request) {
        Author author = new Author();
        author.setName(request.getName());
        author.setBio(request.getBio());
        return mapToResponse(authorRepository.save(author));
    }

    private AuthorResponse mapToResponse(Author author) {
        return AuthorResponse.builder()
                .id(author.getId())
                .name(author.getName())
                .bio(author.getBio())
                .build();
    }
}