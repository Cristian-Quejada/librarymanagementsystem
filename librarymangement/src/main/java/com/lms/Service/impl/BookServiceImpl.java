package com.lms.Service.impl;

import com.lms.repository.BookRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

        Book existingBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookException("Book not found with id: " + bookId));

        existingBook.setActive(false);
        bookRepository.save(existingBook);
    }

    @Override
    public void hardDeleteBook(Long bookId) throws BookException {

        Book existingBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookException("Book not found with id: " + bookId));

        bookRepository.delete(existingBook);
    }

    @Override
    public PageResponse<BookDto> searchBooksWithFilters(BookSearchRequest searchRequest) {

        Pageable pageable = createPageable(searchRequest.getPage(),
                searchRequest.getSize(), 
                searchRequest.getSortBy(), 
                searchRequest.getSortDirection());
        Page<Book> bookPage = bookRepository.searchBooksWithFilters(
            searchRequest.getSearchTerm(),
            searchRequest.getGenreId(),
            searchRequest.getAvailableOnly(),
            PageRequest.of(searchRequest.getPage(), searchRequest.getSize(), 
                Sort.by(Sort.Direction.fromString(searchRequest.getSortDirection()), searchRequest.getSortBy()))
        );
        return convertToPageResponse(bookPage);
    }

    @Override
    public long getTotalActiveBooks() {
        return bookRepository.countByActiveTrue();
    }

    @Override
    public long getTotalAvailableBooks() {
        return bookRepository.countAvailableBooks();
    }


    private Pageable createPageable(int page, int size, String sortBy, String sortDirection) {
        size =Math.min(size, 10);
        size =Math.max(size, 1);

        Sort sort = sortDirection.equalsIgnoreCase("ASC")
        ?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        return PageRequest.of(page, size, sort);
    }


    private PageResponse<BookDto> convertToPageResponse(Page<Book> books) {

        List<BookDto> bookDtos = books.getContent()
                .stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());

        return new PageResponse<>(bookDtos,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isLast(),
                books.isFirst(),
                books.isEmpty()
        );
    }
}
