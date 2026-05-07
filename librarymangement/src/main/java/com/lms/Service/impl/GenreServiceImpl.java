package com.lms.Service.impl;


import org.springframework.stereotype.Service;

import com.lms.Model.Genre;
import com.lms.Service.GenreService;
import com.lms.repository.GenreRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    

    @Override
    public Genre createGenre(Genre genre) {

        return genreRepository.save(genre);
    }

}
