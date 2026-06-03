package com.lms.Service.impl;

import com.lms.mapper.BookLoanMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lms.Model.Book;
import com.lms.Model.BookLoan;
import com.lms.Model.Subscription;
import com.lms.Model.User;
import com.lms.Service.BookLoanService;
import com.lms.Service.SubscriptionService;
import com.lms.Service.UserService;
import com.lms.domain.BookLoanStatus;
import com.lms.domain.BookLoanType;
import com.lms.exception.BookException;
import com.lms.payload.dto.BookLoanDto;
import com.lms.payload.dto.SubscriptionDto;
import com.lms.payload.request.BookLoanSerchRequest;
import com.lms.payload.request.CheckinRequest;
import com.lms.payload.request.CheckoutRequest;
import com.lms.payload.request.RenewalRequest;
import com.lms.payload.response.PageResponse;
import com.lms.repository.BookLoanRepository;
import com.lms.repository.BookRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookLoanServiceImpl implements BookLoanService{

    private final BookLoanMapper bookLoanMapper;
    private final BookLoanRepository bookLoanRepository;
    private final BookRepository bookRepository;
    private final UserService userService;
    private final SubscriptionService subscriptionService;

    
    
    @Override
    public BookLoanDto checkoutBook(CheckoutRequest checkoutRequest) throws Exception {
        User user = userService.getCurrentUser();
        return checkoutBookForUser(user.getId(), checkoutRequest);
    }

    @Override
    public BookLoanDto checkoutBookForUser(Long userId, CheckoutRequest checkoutRequest) throws Exception {
        User user = userService.findById(userId);

        SubscriptionDto subscription = subscriptionService.getUsersActiveSubscription(user.getId());

        Book book = bookRepository.findById(checkoutRequest.getBookId()).orElseThrow(() -> new BookException("Book not found with id" + checkoutRequest.getBookId()));

        if (!book.getActive()) {
            throw new BookException("Book is not active");
        }
        if (book.getAvailableCopies() <= 0) {
            throw new BookException("Book is not available");
        }

        if (bookLoanRepository.hasActiveCheckout(userId, book.getId())) {
            throw new BookException("Book already has active checkout");
        }

        long activeCheckouts = bookLoanRepository.countActiveBookLoansByUser(userId);

        int maxBooksAllowed = subscription.getMaxBooksAllowed();
        
        if (activeCheckouts >= maxBooksAllowed) {
            throw new Exception("You have reached your maximum number of books allowed");
        }

        long overdueCount = bookLoanRepository.countOverdueBookLoansByUser(userId);

        if (overdueCount > 0) {
            throw new Exception("Return first old overdue book");
        }

        BookLoan bookLoan = BookLoan.builder()
                 .user(user)
                 .book(book)
                 .type(BookLoanType.CHECKOUT)
                 .status(BookLoanStatus.CHECKED_OUT)
                 .checkoutDate(LocalDate.now())
                 .dueDate(LocalDate.now().plusDays(checkoutRequest.getCheckoutDays()))
                 .renewalCount(0)
                 .maxRenewals(2)
                 .notes(checkoutRequest.getNotes())
                 .isOverDue(false)
                 .overdueDays(0)
                 .build();

        book.setAvailableCopies(book.getAvailableCopies()-1);
        bookRepository.save(book);

        BookLoan savedBookLoan = bookLoanRepository.save(bookLoan);
        return bookLoanMapper.toDto(savedBookLoan);
    }

    @Override
    public BookLoanDto checkinBook(CheckinRequest checkinRequest) throws Exception {

        BookLoan bookLoan = bookLoanRepository.findById(checkinRequest.getBookLoanId()).orElseThrow(() -> new Exception("Book loan not found"));
        
        if (!bookLoan.isActive()) {
            throw new Exception("Book loan is not active");
        }

        bookLoan.setReturnDate(LocalDate.now());

        BookLoanStatus condition = checkinRequest.getCondition();
        if (condition == null) {
            condition = BookLoanStatus.RETURNED;
        }

        bookLoan.setStatus(condition);

        bookLoan.setOverdueDays(0);
        bookLoan.setIsOverDue(false);

        bookLoan.setNotes("Book returned by user");

        if (condition != BookLoanStatus.LOST) {
            Book book = bookLoan.getBook();
            book.setAvailableCopies(book.getAvailableCopies()+1);
            bookRepository.save(book);
        }

        BookLoan savedBookLoan = bookLoanRepository.save(bookLoan);
        
        return bookLoanMapper.toDto(savedBookLoan);
    }

    @Override
    public BookLoanDto renewCheckout(RenewalRequest renewalRequest) throws Exception {
        BookLoan bookLoan = bookLoanRepository.findById(renewalRequest.getBookLoanId()).orElseThrow(() -> new Exception("Book loan not found!"));

        if (!bookLoan.canRenew()) {
            throw new Exception("Book cannot be renewed!");
        }
        bookLoan.setDueDate(bookLoan.getDueDate().plusDays(renewalRequest.getExtensionDays()));

        bookLoan.setRenewalCount(bookLoan.getRenewalCount()+1);

        bookLoan.setNotes("Book renewed by the user");

        BookLoan savedBookLoan = bookLoanRepository.save(bookLoan);

        return bookLoanMapper.toDto(savedBookLoan);
    }

    @Override
    public PageResponse<BookLoanDto> getMyBookLoans(BookLoanStatus status, int page, int size) throws Exception {
        User currentUser = userService.getCurrentUser();
        Page<BookLoan> bookLoanPage;

        if (status != null) {
            Pageable pageable = PageRequest.of(page, size, Sort.by("dueDate").ascending());
            bookLoanPage = bookLoanRepository.findByStatusAndUser(status, currentUser, pageable);
        } else {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            bookLoanPage = bookLoanRepository.findByUserId(currentUser.getId(), pageable);
        }
        return null;
    }

    @Override
    public PageResponse<BookLoanDto> getBookLoans(BookLoanSerchRequest bookLoanSearchRequest) throws Exception {
        
        Pageable pageable = createPageable(
            bookLoanSearchRequest.getPage(),
            bookLoanSearchRequest.getSize(),
            bookLoanSearchRequest.getSortBy(),
            bookLoanSearchRequest.getSortDirection());

        Page<BookLoan> bookLoanPage;

        if (Boolean.TRUE.equals(bookLoanSearchRequest.getOverdueOnly())) {
            bookLoanPage = bookLoanRepository.findOverdueBookLoans(LocalDate.now(), pageable);
        }
        else if (bookLoanSearchRequest.getUserId() != null) {
            bookLoanPage = bookLoanRepository.findByUserId(bookLoanSearchRequest.getUserId(), pageable);
        }
        else if (bookLoanSearchRequest.getBookId() != null) {
            bookLoanPage = bookLoanRepository.findByBookId(bookLoanSearchRequest.getBookId(), pageable);
        }
        else if (bookLoanSearchRequest.getStatus() != null) {
            bookLoanPage = bookLoanRepository.findByStatus(bookLoanSearchRequest.getStatus(), pageable);
        }
        else if (bookLoanSearchRequest.getStartDate() !=  null && bookLoanSearchRequest.getEndDate() != null) {
            bookLoanPage = bookLoanRepository.findBookLoansByDateRange(
                bookLoanSearchRequest.getStartDate(),
                bookLoanSearchRequest.getEndDate(),
                pageable);
        }
        bookLoanPage = bookLoanRepository.findAll(pageable);
        return convertToPageResponse(bookLoanPage);
        
    }

    @Override
    public int updateOverdueBookLoan() {
        Pageable pageable = PageRequest.of(0, 1000);
        Page<BookLoan> overduePage = bookLoanRepository.findOverdueBookLoans(LocalDate.now(), pageable);

        int updateCount = 0;

        for (BookLoan bookLoan : overduePage.getContent()) {
            if (bookLoan.getStatus() == BookLoanStatus.CHECKED_OUT) {
                bookLoan.setStatus(BookLoanStatus.OVERDUE);
                bookLoan.setIsOverDue(true);

                int overdueDays = calculateOverdueDate(bookLoan.getDueDate(), LocalDate.now());

                // BigDecimal fine = fineCalculationService.calculateOverdueFine(bookLoan);

                bookLoanRepository.save(bookLoan);
                updateCount++;
            }
        }
        return updateCount;
    }

    private Pageable createPageable(int page, int size, String sortBy, String sortDirection) {

        size = Math.min(size, 100);
        size = Math.max(size, 1);

        Sort sort = sortDirection.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return PageRequest.of(page, size, sort);
    }

    private PageResponse<BookLoanDto> convertToPageResponse(Page<BookLoan> bookLoanPage) {

        List<BookLoanDto> bookLoanDtos = bookLoanPage.getContent().stream().map(bookLoanMapper::toDto).collect(Collectors.toList());
        return new PageResponse<>(
            bookLoanDtos,
            bookLoanPage.getNumber(),
            bookLoanPage.getSize(),
            bookLoanPage.getTotalElements(),
            bookLoanPage.getTotalPages(),
            bookLoanPage.isLast(),
            bookLoanPage.isFirst(),
            bookLoanPage.isEmpty()
        );
        
    }

    public int calculateOverdueDate(LocalDate dueDate, LocalDate today) {
        if (today.isBefore(dueDate) || today.isEqual(dueDate)) {
            return 0;
        }
        return (int) ChronoUnit.DAYS.between(dueDate, today);
    }

}
