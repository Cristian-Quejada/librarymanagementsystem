package com.lms.Service.impl;

import com.lms.repository.BookRepository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lms.Model.Book;
import com.lms.Service.BookService;
import com.lms.exception.BookException;
import com.lms.mapper.BookMapper;
import com.lms.payload.dto.BookDto;
import com.lms.payload.request.BookSearchRequest;
import com.lms.payload.response.PageResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto createBook(BookDto bookDto) throws BookException {

        if (bookRepository.existsByIsbn(bookDto.getIsbn())) {
            throw new BookException("book with isbn" + bookDto.getIsbn()+ "already exists");
        }

        Book book = bookMapper.toEntity(bookDto);

        book.isAvailableCopiesValid();
        
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public List<BookDto> createBookBulk(List<BookDto> bookDtos) throws BookException {

        List<BookDto> createdBooks = new ArrayList<>();
        for (BookDto bookDto : bookDtos) {
            BookDto book = createBook(bookDto);
            createdBooks.add(book);
        }
        return createdBooks;
    }

    @Override
    public BookDto getBookById(Long bookId) throws BookException {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookException("Book not found with id: " + bookId) );
        return bookMapper.toDto(book);
    }

    @Override
    public BookDto getBookByIsbn(String isbn) throws BookException {
        
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BookException("Book not found with isbn: " + isbn) );
        return bookMapper.toDto(book);
    }

    @Override
    public BookDto updateBook(Long bookId, BookDto bookDto) throws BookException {
        
        Book existingBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookException("Book not found with id: " + bookId));

        bookMapper.updateEntityFromDto(bookDto, existingBook);
        existingBook.isAvailableCopiesValid();
        Book updatedBook = bookRepository.save(existingBook);
        return bookMapper.toDto(updatedBook); 
    }

    @Override
    public void deleteBook(Long bookId) throws BookException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteBook'");
    }

    @Override
    public void hardDeleteBook(Long bookId) throws BookException {
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
