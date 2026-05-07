package com.lms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.Model.Genre;
import com.lms.Service.GenreService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/genres")
public class GenreController {

    private final GenreService genreService;

    @PostMapping("/create")
    public ResponseEntity<Genre> createGenre(@RequestBody Genre genre) {
        Genre createdGenre = genreService.createGenre(genre);
        return ResponseEntity.ok(createdGenre);
    }
}
