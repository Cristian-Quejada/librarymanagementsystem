package com.lms.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lms.exception.GenreException;
import com.lms.payload.dto.GenreDto;

@Service
public interface GenreService {


    GenreDto createGenre(GenreDto genre);

    List<GenreDto> getAllGenres();

    GenreDto getGenreById(Long id) throws GenreException;

    GenreDto updateGenre(Long id, GenreDto genre) throws GenreException;

    void deleteGenre(Long id) throws GenreException;

    void hardDeleteGenre(Long id) throws GenreException;

    List<GenreDto> getAllActiveGenresWithSubGenres();

    List<GenreDto> getTopLevelGenres();

    //Page<GenreDto> searchGenres(String searchText, Pageable pageable);

    long getTotalActiveGenresCount();

    long getBookCountByGenreId(Long genreId);
}
