package com.lms.Service.impl;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.lms.Model.Genre;
import com.lms.Service.GenreService;
import com.lms.mapper.GenreMapper;
import com.lms.payload.dto.GenreDto;
import com.lms.repository.GenreRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    

    @Override
    public GenreDto createGenre(GenreDto genreDto) {

       // return genreRepository.save(genreDto);

       Genre genre = Genre.builder()
                .code(genreDto.getCode())
                .name(genreDto.getName())
                .description(genreDto.getDescription())
                .displayOrder(genreDto.getDisplayOrder())
                .active(true)
                .build();

        if (genreDto.getParentGenreId() != null) {
            Genre parentGenre = genreRepository.findById(genreDto.getParentGenreId())
                    .orElseThrow(() -> new RuntimeException("Parent genre not found with id: " + genreDto.getParentGenreId()));
            genre.setParentGenre(parentGenre);
        }

        Genre savedGenre = genreRepository.save(genre);

        GenreDto dto = GenreMapper.toDto(savedGenre);

                //dto.setSubGenres(savedGenre.getSubGenres().stream()
                //        .filter(subGenre -> subGenre.getActive())
                //        .map(subGenre -> GenreDto.builder()
                //                .id(subGenre.getId())
                //                .code(subGenre.getCode())
                //                .name(subGenre.getName())
                //                .description(subGenre.getDescription())
                //                .displayOrder(subGenre.getDisplayOrder())
                //                .active(subGenre.getActive())
                //                .build())
                //        .toList());
       return dto;
    }



    @Override
    public List<GenreDto> getAllGenres() {
        
        return genreRepository.findAll().stream()
                .map(GenreMapper::toDto)
                .collect(Collectors.toList());
    }

}
