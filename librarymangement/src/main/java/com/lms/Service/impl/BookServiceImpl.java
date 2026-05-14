package com.lms.Service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lms.Service.BookService;
import com.lms.payload.dto.BookDto;
import com.lms.payload.request.BookSearchRequest;
import com.lms.payload.response.PageResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    @Override
    public BookDto createBook(BookDto bookDto) {

        return null;
    }

    @Override
    public List<BookDto> createBookBulk() {

        return null;
    }

    @Override
    public BookDto getBookById(Long bookId) {

        return null;
    }

    @Override
    public BookDto getBookByIsbn(String isbn) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBookByIsbn'");
    }

    @Override
    public BookDto updateBook(Long bookId, BookDto bookDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateBook'");
    }

    @Override
    public void deleteBook(Long bookId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteBook'");
    }

    @Override
    public void hardDeleteBook(Long bookId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hardDeleteBook'");
    }

    @Override
    public PageResponse<BookDto> searchBooksWithFilters(BookSearchRequest searchRequest) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchBooksWithFilters'");
    }

    @Override
    public long getTotalActiveBooks() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTotalActiveBooks'");
    }

    @Override
    public long getTotalAvailableBooks() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTotalAvailableBooks'");
    }

}
