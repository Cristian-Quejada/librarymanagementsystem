package com.lms.Service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.lms.payload.dto.BookDto;
import com.lms.payload.request.BookSearchRequest;
import com.lms.payload.response.PageResponse;

public interface BookService {

    BookDto createBook(BookDto bookDto);

    List<BookDto> createBookBulk();

    BookDto getBookById(Long bookId);

    BookDto getBookByIsbn(String isbn);

    BookDto updateBook(Long bookId, BookDto bookDto);

    void deleteBook(Long bookId);

    void hardDeleteBook(Long bookId);

    PageResponse<BookDto> searchBooksWithFilters(
        BookSearchRequest searchRequest
    );

    long getTotalActiveBooks();

    long getTotalAvailableBooks();
}
