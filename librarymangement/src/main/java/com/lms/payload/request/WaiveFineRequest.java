package com.lms.payload.request;

import java.util.List;

import com.lms.domain.FineStatus;
import com.lms.domain.FineType;
import com.lms.payload.dto.FineDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WaiveFineRequest {

    @NotNull(message = "Fine ID is mandatory")
    private Long fineId;

    @NotBlank(message = "Waiver reason is mandatory")
    private String reason;

}
