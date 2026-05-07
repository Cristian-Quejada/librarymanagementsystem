package com.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.lms.Model.Genre;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long>{

    
}
