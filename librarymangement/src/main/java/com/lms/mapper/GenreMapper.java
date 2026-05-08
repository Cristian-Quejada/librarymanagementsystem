package com.lms.mapper;

import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.lms.Model.Genre;
import com.lms.payload.dto.GenreDto;

public class GenreMapper {

    public static GenreDto toDto(Genre savedGenre) {
        if (savedGenre == null) {
            return null;
        }
        GenreDto dto = GenreDto.builder()
                .id(savedGenre.getId())
                .code(savedGenre.getCode())
                .name(savedGenre.getName())
                .description(savedGenre.getDescription())
                .displayOrder(savedGenre.getDisplayOrder())
                .active(savedGenre.getActive())
                .createdAt(savedGenre.getCreatedAt())
                .updatedAt(savedGenre.getUpdatedAt())
                .build();

        if (savedGenre.getParentGenre() != null) {
            dto.setParentGenreId(savedGenre.getParentGenre().getId());
            dto.setParentGenreName(savedGenre.getParentGenre().getName());
        }

        if (savedGenre.getSubGenres() != null && !savedGenre.getSubGenres().isEmpty()) {
            dto.setSubGenres(savedGenre.getSubGenres().stream()
                .filter(subGenre -> subGenre.getActive())
                .map(subGenre -> toDto(subGenre)).collect(Collectors.toList()));    
        }
        
        return dto;

        
    }
}
