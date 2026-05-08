package com.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.lms.Model.Genre;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long>{

    List<Genre> findByActiveTrueOrderByDisplayOrderAsc();

    List<Genre> findByParentGenreIsNullAndActiveTrueOrderByDisplayOrderAsc();

    List<Genre> findByParentGenreIdAndActiveTrueOrderByDisplayOrder(Long parentGenreId);

    long countByActiveTrue();

    //@Query("select count(b) from book b where b.genre.id = :genreId")
    //long countBooksByGenre(@Param("genreId") Long genreId);
}
