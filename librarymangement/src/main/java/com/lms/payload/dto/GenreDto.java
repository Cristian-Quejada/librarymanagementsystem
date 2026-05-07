package com.lms.payload.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenreDto {

    private Long id;

    @NotBlank(message = "Code is mandatory")
    private String code;

    @NotBlank(message = "Name is mandatory")
    private String name;
    
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @Min(value = 0, message = "Display order must be a positive integer")
    private Integer displayOrder;

    private Boolean active;
}
