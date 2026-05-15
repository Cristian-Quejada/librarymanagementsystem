package com.lms.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.Service.BookService;
import com.lms.exception.BookException;
import com.lms.exception.UserException;
import com.lms.payload.dto.BookDto;
import com.lms.payload.request.BookSearchRequest;
import com.lms.payload.response.ApiResponse;
import com.lms.payload.response.PageResponse;

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

    @GetMapping("/{bookId}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long bookId) throws BookException  {
        BookDto book = bookService.getBookById(bookId);
        return ResponseEntity.ok(book);
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<BookDto> updateBook(
            @PathVariable Long bookId, 
            @Valid @RequestBody BookDto bookDto) throws BookException {
            
            BookDto updatedBook = bookService.updateBook(bookId, bookDto);
            return ResponseEntity.ok(updatedBook);
        
    }


    @DeleteMapping("/{bookId}")
    public ResponseEntity<ApiResponse> deleteBook(@PathVariable Long bookId) throws BookException {
        bookService.deleteBook(bookId);
        ApiResponse response = new ApiResponse("Book deleted - soft delete", true);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{bookId}/pernanent")
    public ResponseEntity<ApiResponse> hardDeleteBook(@PathVariable Long bookId) throws BookException {
        bookService.hardDeleteBook(bookId);
        ApiResponse response = new ApiResponse("Book deleted - hard delete", true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/search")
    public ResponseEntity<PageResponse<BookDto>> advancedSearch(@RequestBody BookSearchRequest searchRequest) {
        
        PageResponse<BookDto> books = bookService.searchBooksWithFilters(searchRequest);
        return ResponseEntity.ok(books);
        
    }

    public ResponseEntity<BookStateResponse> getBookState() {
        long totalActiveBooks = bookService.getTotalActiveBooks();
        long totalAvailableBooks = bookService.getTotalAvailableBooks();
        BookStateResponse response = new BookStateResponse(totalActiveBooks, totalAvailableBooks);
        return ResponseEntity.ok(response);
    }


    public static class BookStateResponse {
    
        public long totalActiveBooks;
        public long totalAvailableBooks;

        public BookStateResponse(long totalActiveBooks, long totalAvailableBooks) {
            this.totalActiveBooks = totalActiveBooks;
            this.totalAvailableBooks = totalAvailableBooks;
        }
        
    }

}
