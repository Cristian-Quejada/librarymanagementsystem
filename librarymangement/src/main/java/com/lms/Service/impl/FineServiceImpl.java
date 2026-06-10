package com.lms.Service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lms.Model.BookLoan;
import com.lms.Model.Fine;
import com.lms.Model.User;
import com.lms.Service.FineService;
import com.lms.Service.PaymentService;
import com.lms.Service.UserService;
import com.lms.domain.FineStatus;
import com.lms.domain.FineType;
import com.lms.domain.PaymentGateway;
import com.lms.domain.PaymentType;
import com.lms.mapper.FineMapper;
import com.lms.payload.dto.FineDto;
import com.lms.payload.request.CreateFineRequest;
import com.lms.payload.request.PaymentInitiateRequest;
import com.lms.payload.request.WaiveFineRequest;
import com.lms.payload.response.PageResponse;
import com.lms.payload.response.PaymentInitiateResponse;
import com.lms.repository.BookLoanRepository;
import com.lms.repository.FineRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FineServiceImpl implements FineService {

    private final BookLoanRepository bookLoanRepository;

    private final FineRepository fineRepository;

    private final FineMapper fineMapper;

    private final UserService userService;

    private final PaymentService paymentService;

    @Override
    public FineDto createFine(CreateFineRequest createFineRequest) {

        BookLoan bookLoan = bookLoanRepository.findById(createFineRequest.getBookLoanId()).orElseThrow(() -> new RuntimeException("Book loan does not exist"));

        Fine fine = Fine.builder()
            .bookLoan(bookLoan)
            .user(bookLoan.getUser())
            .type(createFineRequest.getType())
            .amount(createFineRequest.getAmount())
            .status(FineStatus.PENDING)
            .reason(createFineRequest.getReason())
            .note(createFineRequest.getNotes())
            .build();
        Fine savedFine = fineRepository.save(fine);
        return fineMapper.toDto(savedFine);
    }

    @Override
    public PaymentInitiateResponse payFine(Long fineId, String transactionId) throws Exception {

        Fine fine = fineRepository.findById(fineId).orElseThrow(() -> new Exception("Book loan doesn't exist"));
        if (fine.getStatus().equals(FineStatus.PAID)) {
            throw new Exception("Fine already paid");
        }
        if (fine.getStatus().equals(FineStatus.WAIVED)) {
            throw new Exception("Fine waived");
        }

        User user = userService.getCurrentUser();
        PaymentInitiateRequest request = PaymentInitiateRequest.builder()
            .userId(user.getId())
            .fineId(fine.getId())
            .paymentType(PaymentType.FINE)
            .gateway(PaymentGateway.RAZORPAY)
            .amount(fine.getAmount())
            .description("Library fine payment")
            .build();
        return paymentService.initiatePayment(request);
    }

    @Override
    public void markFineAsPaid(Long fineId, Long amount, String transactionId) throws Exception {
        Fine fine = fineRepository.findById(fineId).orElseThrow(() -> new Exception("Fine not found with id: " + fineId));

        fine.applyPayment(amount);
        fine.setTransactionId(transactionId);
        fine.setStatus(FineStatus.PAID);
        fine.setUpdatedAt(LocalDateTime.now());

        fineRepository.save(fine);
    }

    @Override
    public FineDto waiveFine(WaiveFineRequest waiveFineRequest) throws Exception {
        Fine fine = fineRepository.findById(waiveFineRequest.getFineId()).orElseThrow(() -> new Exception("Fine not found with id:" + waiveFineRequest.getFineId()));
        
        if (fine.getStatus() == FineStatus.WAIVED) {
            throw new Exception("Fine has already waived");
        }

        if (fine.getStatus() == FineStatus.PAID) {
            throw new Exception("Fine has already paid and cannot be waived");
        }

        User currentAdmin = userService.getCurrentUser();
        fine.waive(currentAdmin, waiveFineRequest.getReason());

        Fine savedFine = fineRepository.save(fine);

        return fineMapper.toDto(savedFine);
    }

    @Override
    public List<FineDto> getMyFines(FineStatus status, FineType type) throws Exception {
        User currentUser = userService.getCurrentUser();
        List<Fine> fines;

        if (status != null && type != null) {
            fines = fineRepository.findByUserId(currentUser.getId()).stream().filter(f -> f.getStatus() == status
                    && f.getType() == type).collect(Collectors.toList());
        } else if (status != null) {
            fines = fineRepository.findByUserId(currentUser.getId()).stream().filter(f -> f.getStatus() == status
                    ).collect(Collectors.toList());
        } else if (type != null) {
            fines = fineRepository.findByUserIdAndType(currentUser.getId(), type);
        } else {
            fines = fineRepository.findByUserId(currentUser.getId());
        }
        return fines.stream().map(fineMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public PageResponse<FineDto> getAllFines(FineStatus status, FineType type, Long userId, int page, int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        
        Page<Fine> finePage = fineRepository.findAllWithFilters(userId, status, type, pageable);
        
        return convertToPageResponse(finePage);
    }

    private PageResponse<FineDto> convertToPageResponse(Page<Fine> finePage) {
        List<FineDto> fineDtos = finePage.getContent().stream().map(fineMapper::toDto).collect(Collectors.toList());
        return new PageResponse<>(
            fineDtos,
            finePage.getNumber(),
            finePage.getSize(),
            finePage.getTotalElements(),
            finePage.getTotalPages(),
            finePage.isLast(),
            finePage.isFirst(),
            finePage.isEmpty()
        );
    }

}
