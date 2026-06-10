package com.lms.mapper;

import org.springframework.stereotype.Component;

import com.lms.Model.Fine;
import com.lms.payload.dto.FineDto;

@Component
public class FineMapper {

    public FineDto toDto(Fine fine) {
        if (fine == null) {
            return null;
        }

        FineDto dto = new FineDto();
        dto.setId(fine.getId());

        if (fine.getBookLoan() != null) {
            dto.setBookLoanId(fine.getBookLoan().getId());
            if (fine.getBookLoan().getBook() != null) {
                dto.setBookTitle(fine.getBookLoan().getBook().getTitle());
                dto.setBookIsbn(fine.getBookLoan().getBook().getIsbn());

            }
        }

        if (fine.getUser() != null) {
            dto.setUserId(fine.getUser().getId());
            dto.setUserName(fine.getUser().getFullName());
            dto.setUserEmail(fine.getUser().getEmail());
        }

        dto.setType(fine.getType());
        dto.setAmount(fine.getAmount());

        dto.setStatus(fine.getStatus());
        dto.setReason(fine.getReason());
        dto.setNotes(fine.getNote());

        if (fine.getWaivedBy() != null) {
            dto.setWaivedByUserId(fine.getWaivedBy().getId());
            dto.setWaivedByUserName(fine.getWaivedBy().getFullName());
        }
        dto.setWaivedAt(fine.getWaivedAt());
        dto.setWaiverReason(fine.getWaiverReason());

        dto.setPaidAt(fine.getPaidAt());
        if (fine.getProcessedBy() != null) {
            dto.setProcessedByUserId(fine.getProcessedBy().getId());
            dto.setProcessedByUserName(fine.getProcessedBy().getFullName());
        }
        dto.setTransactionId(fine.getTransactionId());

        dto.setCreatedAt(fine.getCreatedAt());
        dto.setUpdatedAt(fine.getUpdatedAt());

        return dto;
    }
}
