package com.lms.Service;

import java.util.List;

import com.lms.domain.FineStatus;
import com.lms.domain.FineType;
import com.lms.payload.dto.FineDto;
import com.lms.payload.request.CreateFineRequest;
import com.lms.payload.request.WaiveFineRequest;
import com.lms.payload.response.PageResponse;
import com.lms.payload.response.PaymentInitiateResponse;

public interface FineService {

    FineDto createFine(CreateFineRequest createFineRequest);

    PaymentInitiateResponse payFine(Long fineId, String  transactionId) throws Exception;

    void markFineAsPaid(Long fineId, Long amount, String transactionId) throws Exception;

    FineDto waiveFine(WaiveFineRequest waiveFineRequest) throws Exception;

    List<FineDto> getMyFines(FineStatus status, FineType type) throws Exception;

    PageResponse<FineDto> getAllFines(
        FineStatus status,
        FineType type,
        Long userId,
        int page,
        int size
    );
}
