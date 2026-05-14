package com.lms.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lms.Model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    Boolean existsByIsbn(String isbn);

    @Query("select b from Book b where" + "b.active = true and " + 
        "(coalesce(:searchTerm) is null or lower(b.title) like lower(concat('%', :searchTerm, '%')) or lower(b.author) like lower(concat('%', :searchTerm, '%'))) and " +
        "(coalesce(:genreId) is null or b.genre.id = :genreId) and " +
        "(coalesce(:availableOnly) is null or (b.availableCopies > 0))")
    Page<Book> searchBooksWithFilters (
        @Param("searchTerm") String searchTerm, 
        @Param("genreId") Long genreId, 
        @Param("availableOnly") Boolean availableOnly,
        Pageable pageable);
}
