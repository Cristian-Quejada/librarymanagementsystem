package com.lms.Service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.lms.exception.BookException;
import com.lms.payload.dto.BookDto;
import com.lms.payload.request.BookSearchRequest;
import com.lms.payload.response.PageResponse;

public interface BookService {

    BookDto createBook(BookDto bookDto) throws BookException;

    List<BookDto> createBookBulk(List<BookDto> bookDtos) throws BookException;

    BookDto getBookById(Long bookId) throws BookException;

    BookDto getBookByIsbn(String isbn) throws BookException;

    BookDto updateBook(Long bookId, BookDto bookDto) throws BookException;

    void deleteBook(Long bookId) throws BookException;

    void hardDeleteBook(Long bookId) throws BookException;

    PageResponse<BookDto> searchBooksWithFilters(
        BookSearchRequest searchRequest
    );

    long getTotalActiveBooks();

    long getTotalAvailableBooks();
}
