package com.lms.mapper;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.lms.Model.Genre;
import com.lms.payload.dto.GenreDto;
import com.lms.repository.GenreRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GenreMapper {
    private final GenreRepository genreRepository;
    public GenreDto toDto(Genre savedGenre) {
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
    public Genre toEntity(GenreDto genreDto) {
        if (genreDto == null) {
            return null;
        }
        Genre genre = Genre.builder()
                .code(genreDto.getCode())
                .name(genreDto.getName())
                .description(genreDto.getDescription())
                .displayOrder(genreDto.getDisplayOrder())
                .active(true)
                .build();

        if (genreDto.getParentGenreId() != null) {
            genreRepository.findById(genreDto.getParentGenreId())
                    .ifPresent(genre::setParentGenre);
        }
        return genre;
    }

    public void updateEntityFromDto(GenreDto dto, Genre existingGenre) {
        if (dto == null || existingGenre == null) {
            return;
        }
        existingGenre.setCode(dto.getCode());
        existingGenre.setName(dto.getName());
        existingGenre.setDescription(dto.getDescription());
        existingGenre.setDisplayOrder(dto.getDisplayOrder() != null ? dto.getDisplayOrder() : 0);
        
        if (dto.getActive() != null) {
            existingGenre.setActive(dto.getActive());
        }
        if (dto.getParentGenreId() != null) {
            genreRepository.findById(dto.getParentGenreId())
                    .ifPresent(existingGenre::setParentGenre);
        }
    }

    public List<GenreDto> toDtoList(List<Genre> genreList) {
     
        return genreList.stream().map(genre -> toDto(genre)).collect(Collectors.toList());
    }
}
