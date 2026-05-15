package com.lms.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.Service.BookService;
import com.lms.exception.BookException;
import com.lms.payload.dto.BookDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookDto> createBook(@Valid @RequestBody BookDto bookDto) throws BookException {
        BookDto createdBook = bookService.createBook(bookDto);
        return ResponseEntity.ok(createdBook);
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> createBooksBulk(@Valid @RequestBody List<BookDto> bookDtos) throws BookException {
        List<BookDto> createdBooks = bookService.createBookBulk(bookDtos);
        return ResponseEntity.ok(createdBooks);
    }
}
