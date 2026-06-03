package com.lms.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lms.Service.BookLoanService;
import com.lms.domain.BookLoanStatus;
import com.lms.payload.dto.BookLoanDto;
import com.lms.payload.request.BookLoanSerchRequest;
import com.lms.payload.request.CheckinRequest;
import com.lms.payload.request.CheckoutRequest;
import com.lms.payload.request.RenewalRequest;
import com.lms.payload.response.ApiResponse;
import com.lms.payload.response.PageResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book-loans")
public class BookLoanController {

    private final BookLoanService bookLoanService;

    @PostMapping("/checkout")
    public ResponseEntity<?> checkoutBook(@Valid @RequestBody CheckoutRequest request) throws Exception {
        
        BookLoanDto bookloan = bookLoanService.checkoutBook(request);
        
        return new ResponseEntity<>(bookloan, HttpStatus.CREATED);
    }

    @PostMapping("/checkout/user/{userId}")
    public ResponseEntity<?> checkoutBookForUser(@PathVariable Long userId, 
                                        @Valid @RequestBody CheckoutRequest checkoutRequest) throws Exception {
        BookLoanDto bookLoan = bookLoanService.checkoutBookForUser(userId, checkoutRequest);
        
        return new ResponseEntity<>(bookLoan, HttpStatus.CREATED);

    }

    @PostMapping("/checkin")
    public ResponseEntity<?> checKin(@Valid @RequestBody CheckinRequest checkinRequest) throws Exception {
        BookLoanDto bookLoan = bookLoanService.checkinBook(checkinRequest);
        
        return new ResponseEntity<>(bookLoan, HttpStatus.CREATED);

    }

    @PostMapping("/renew")
    public ResponseEntity<?> renew(@Valid @RequestBody RenewalRequest renewalRequest) throws Exception {
        BookLoanDto bookLoan = bookLoanService.renewCheckout(renewalRequest);
        
        return new ResponseEntity<>(bookLoan, HttpStatus.OK);

    }

    @GetMapping("/myBookLoans")
    public ResponseEntity<?> getMyBookLoans(@RequestParam(required = false) BookLoanStatus status,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "20") int size) throws Exception {

        PageResponse<BookLoanDto> bookLoans = bookLoanService.getMyBookLoans(status, page, size);
        
        return ResponseEntity.ok(bookLoans);
                                            
    }

    @PostMapping("/search")
    public ResponseEntity<?> getAllBookLoans(@RequestBody BookLoanSerchRequest bookLoanSearchRequest) throws Exception {

        PageResponse<BookLoanDto> bookLoans = bookLoanService.getBookLoans(bookLoanSearchRequest);
        return ResponseEntity.ok(bookLoans);

    }

    @PostMapping("/admin/update-overdue")
    public ResponseEntity<?> updateOverdueBooksLoan() {
        int updateCount = bookLoanService.updateOverdueBookLoan();
        return ResponseEntity.ok(new ApiResponse("Overdue book loans updated", true));
    }
}
