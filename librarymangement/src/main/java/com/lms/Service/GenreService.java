package com.lms.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lms.payload.dto.GenreDto;

@Service
public interface GenreService {


    GenreDto createGenre(GenreDto genre);

    List<GenreDto> getAllGenres();
}
