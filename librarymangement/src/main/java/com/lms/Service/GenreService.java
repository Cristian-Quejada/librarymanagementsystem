package com.lms.Service;

import org.springframework.stereotype.Service;

import com.lms.Model.Genre;

@Service
public interface GenreService {


    Genre createGenre(Genre genre);
}
