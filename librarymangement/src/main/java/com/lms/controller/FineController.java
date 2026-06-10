package com.lms.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lms.Service.FineService;
import com.lms.domain.FineStatus;
import com.lms.domain.FineType;
import com.lms.payload.dto.FineDto;
import com.lms.payload.request.CreateFineRequest;
import com.lms.payload.request.WaiveFineRequest;
import com.lms.payload.response.PageResponse;
import com.lms.payload.response.PaymentInitiateResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fines")
public class FineController {

    private final FineService fineService;

    @PostMapping
    public ResponseEntity<?> createFine(@Valid @RequestBody CreateFineRequest fineRequest) throws Exception {
        FineDto fineDto = fineService.createFine(fineRequest);

        return ResponseEntity.ok(fineDto);
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<?> payFine(@PathVariable Long id, @RequestParam(required = false) String transactionId) throws Exception {
        PaymentInitiateResponse res = fineService.payFine(id, transactionId);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/waive")
    public ResponseEntity<?> waiveFine(@Valid @RequestBody WaiveFineRequest waiveFineRequest) throws Exception {
        FineDto fineDto = fineService.waiveFine(waiveFineRequest);

        return ResponseEntity.ok(fineDto);
    }

    @GetMapping("/myFines")
    public ResponseEntity<?> getMyFines(@RequestParam(required = false) FineStatus status,
                                        @RequestParam(required = false) FineType type) throws Exception {

                                        List<FineDto> fines = fineService.getMyFines(status, type);

                                        return ResponseEntity.ok(fines);
                                            
    }

    @GetMapping
    public ResponseEntity<?> getAllFines(@RequestParam(required = false) FineStatus status,
                                         @RequestParam(required = false) FineType type,
                                         @RequestParam(required = false) Long userId,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "20") int size) {

                                         PageResponse<FineDto> fines = fineService.getAllFines(status, type, userId, page, size);
                                         return ResponseEntity.ok(fines);
                                         }


}
