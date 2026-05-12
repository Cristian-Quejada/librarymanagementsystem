package com.lms.payload.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto {

    private Long id;

    @NotBlank(message = "ISBN is mandatory")
    private String isbn;

    @NotBlank(message = "Title is mandatory")
    @Size(min = 1, max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @NotBlank(message = "Author is mandatory")
    @Size(min = 1, max = 255, message = "Author name must not exceed 255 characters")
    private String author;


    @NotNull(message = "Genre is mandatory")
    private Long genreId;

    private String genreName;

    private String genreCode;

    private GenreDto genre;

    @Size(max = 100, message = "Publisher name must not exceed 100 characters")
    private String publisher;
    
    private LocalDate publishedDate;


    @Size(max = 20, message = "Language must not exceed 20 characters")
    private String language;

    @Min(value = 1, message = "Pages must be at least 1")
    @Max(value = 50000, message = "Pages must not exceed 50000")
    private Integer pages;

    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;
    
    @Min(value = 0, message = "Total copies must be a positive integer")
    @NotNull(message = "Total copies is mandatory")
    private Integer totalCopies;

    @Min(value = 0, message = "Available copies must be a positive integer")
    @NotNull(message = "Available copies is mandatory")
    private Integer availableCopies;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be a positive number")
    @Digits(integer = 10, fraction = 2, message = "Price must be a valid monetary amount")
    private BigDecimal price;

    @Size(max = 500, message = "Cover image URL must not exceed 500 characters")
    private String coverImageUrl;

    private Boolean alreadyHaveLoan;

    private Boolean alredyHaveReservation;

    private Boolean active;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
