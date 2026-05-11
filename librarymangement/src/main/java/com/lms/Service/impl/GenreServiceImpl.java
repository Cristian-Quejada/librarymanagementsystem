package com.lms.Service.impl;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.lms.Model.Genre;
import com.lms.Service.GenreService;
import com.lms.exception.GenreException;
import com.lms.mapper.GenreMapper;
import com.lms.payload.dto.GenreDto;
import com.lms.repository.GenreRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    private final GenreMapper genreMapper;

    @Override
    public GenreDto createGenre(GenreDto genreDto) {

       Genre genre = genreMapper.toEntity(genreDto);

        Genre savedGenre = genreRepository.save(genre);

        return genreMapper.toDto(savedGenre);
    }



    @Override
    public List<GenreDto> getAllGenres() {
        
        return genreRepository.findAll().stream()
                .map(genreMapper::toDto)
                .collect(Collectors.toList());
    }



    @Override
    public GenreDto getGenreById(Long genreId) throws GenreException {
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new GenreException("Genre not found with id: " + genreId));

        return genreMapper.toDto(genre);
    }



    @Override
    public GenreDto updateGenre(Long genreId, GenreDto genreDto) throws GenreException {

        Genre existingGenre = genreRepository.findById(genreId)
                .orElseThrow(() -> new GenreException("Genre not found with id: " + genreId));

        genreMapper.updateEntityFromDto(genreDto, existingGenre);
        Genre updatedGenre = genreRepository.save(existingGenre);
        return genreMapper.toDto(updatedGenre);
    }



    @Override
    public void deleteGenre(Long genreId) throws GenreException {

        Genre existingGenre = genreRepository.findById(genreId)
                .orElseThrow(() -> new GenreException("Genre not found with id: " + genreId));

                existingGenre.setActive(false);
                genreRepository.save(existingGenre);

    }



    @Override
    public void hardDeleteGenre(Long genreId) throws GenreException {
        
        Genre existingGenre = genreRepository.findById(genreId)
                .orElseThrow(() -> new GenreException("Genre not found with id: " + genreId));

                genreRepository.delete(existingGenre);
    }



    @Override
    public List<GenreDto> getAllActiveGenresWithSubGenres() {
        List<Genre> topLevelGenres = genreRepository
                    .findByParentGenreIsNullAndActiveTrueOrderByDisplayOrderAsc();

                    return genreMapper.toDtoList(topLevelGenres);
    }



    @Override
    public List<GenreDto> getTopLevelGenres() {
        List<Genre> topLevelGenres = genreRepository
                    .findByParentGenreIsNullAndActiveTrueOrderByDisplayOrderAsc();

                    return genreMapper.toDtoList(topLevelGenres);
    }



    @Override
    public long getTotalActiveGenresCount() {

        return genreRepository.countByActiveTrue();
    }



    @Override
    public long getBookCountByGenreId(Long genreId) {
        return 0;
     
    }

}
