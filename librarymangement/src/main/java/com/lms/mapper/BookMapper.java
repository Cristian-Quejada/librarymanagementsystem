package com.lms.mapper;

import org.springframework.stereotype.Component;

import com.lms.Model.Book;
import com.lms.Model.Genre;
import com.lms.exception.BookException;
import com.lms.payload.dto.BookDto;
import com.lms.repository.GenreRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BookMapper {

    private final GenreRepository genreRepository;

    public BookDto toDto(Book book) {
        if (book == null) {
            return null;
        }
        return BookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .genreId(book.getGenre().getId())
                .genreName(book.getGenre().getName())
                .genreCode(book.getGenre().getCode())
                .publisher(book.getPublisher())
                .publishedDate(book.getPublishedDate())
                .language(book.getLanguage())
                .pages(book.getPages())
                .description(book.getDescription())
                .totalCopies(book.getTotalCopies())
                .availableCopies(book.getAvailableCopies())
                .price(book.getPrice())
                .coverImageUrl(book.getCoverImageUrl())
                .active(book.getActive())
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .build();
    }

    public Book toEntity(BookDto bookDto) throws BookException {
        if (bookDto == null) {
            return null;
        }

        Book book = new Book();
        book.setId(bookDto.getId());
        book.setIsbn(bookDto.getIsbn());
        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());

        if (bookDto.getGenreId() !=null) {
            Genre genre = genreRepository.findById(bookDto.getGenreId())
                    .orElseThrow(() -> new BookException("Genre not found with id: " + bookDto.getGenreId()));
            book.setGenre(genre);
        }
        book.setPublisher(bookDto.getPublisher());
        book.setPublishedDate(bookDto.getPublishedDate());
        book.setLanguage(bookDto.getLanguage());
        book.setPages(bookDto.getPages());
        book.setDescription(bookDto.getDescription());
        book.setTotalCopies(bookDto.getTotalCopies());
        book.setAvailableCopies(bookDto.getAvailableCopies());
        book.setPrice(bookDto.getPrice());
        book.setCoverImageUrl(bookDto.getCoverImageUrl());
        book.setActive(true);


        return book;
    }

    public void updateEntityFromDto(BookDto dto, Book existingBook) throws BookException {
        if (dto == null || existingBook == null) {
            return;
        }

        existingBook.setTitle(dto.getTitle());
        existingBook.setAuthor(dto.getAuthor());

        if (dto.getGenreId() != null) {
            Genre genre = genreRepository.findById(dto.getGenreId())
                    .orElseThrow(() -> new BookException("Genre not found with id: " + dto.getGenreId()));
            existingBook.setGenre(genre);
        }
        existingBook.setPublisher(dto.getPublisher());
        existingBook.setPublishedDate(dto.getPublishedDate());
        existingBook.setLanguage(dto.getLanguage());
        existingBook.setPages(dto.getPages());
        existingBook.setDescription(dto.getDescription());
        existingBook.setTotalCopies(dto.getTotalCopies());
        existingBook.setAvailableCopies(dto.getAvailableCopies());
        existingBook.setPrice(dto.getPrice());
        existingBook.setCoverImageUrl(dto.getCoverImageUrl());

        if (dto.getActive() != null) {
            existingBook.setActive(dto.getActive());
        }
    }
}
