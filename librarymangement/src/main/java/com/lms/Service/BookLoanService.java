package com.lms.Service;

import com.lms.domain.BookLoanStatus;
import com.lms.payload.dto.BookLoanDto;
import com.lms.payload.request.BookLoanSerchRequest;
import com.lms.payload.request.CheckinRequest;
import com.lms.payload.request.CheckoutRequest;
import com.lms.payload.request.RenewalRequest;
import com.lms.payload.response.PageResponse;

public interface BookLoanService {

    BookLoanDto checkoutBook(CheckoutRequest checkoutRequest) throws Exception;

    BookLoanDto checkoutBookForUser(Long userId, CheckoutRequest checkoutRequest) throws Exception;

    BookLoanDto checkinBook(CheckinRequest checkinRequest) throws Exception;

    BookLoanDto renewCheckout(RenewalRequest renewalRequest) throws Exception;

    PageResponse<BookLoanDto> getMyBookLoans(BookLoanStatus status, int page, int size) throws Exception;

    PageResponse<BookLoanDto> getBookLoans(BookLoanSerchRequest bookLoanSearchRequest) throws Exception;

    int updateOverdueBookLoan();

}
