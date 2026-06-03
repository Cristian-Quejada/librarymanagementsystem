package com.lms.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lms.Model.BookLoan;
import com.lms.Model.User;
import com.lms.domain.BookLoanStatus;

public interface BookLoanRepository extends JpaRepository<BookLoan, Long>{

    Page<BookLoan> findByUserId(Long userId, Pageable pageable);
    Page<BookLoan> findByStatusAndUser(BookLoanStatus status, User user, Pageable pageable);
    Page<BookLoan> findByStatus(BookLoanStatus status, Pageable pageable);

    Page<BookLoan> findByBookId(Long bookId, Pageable pageable);

    @Query("select case when count(bl) > 0 then true else false end from BookLoan bl" +
        " where bl.user.id =:userId and bl.book.id =:bookId " +
        " and (bl.status = 'CHECKED_OUT' OR bl.status = 'OVERDUE')"
    )
    boolean hasActiveCheckout(@Param("userId") Long userId,
                              @Param("bookId") Long bookId);

    @Query("SELECT COUNT(bl) from BookLoan bl where bl.user.id = :userId " +
           " AND (bl.status = 'CHECKED_OUT' OR bl.status = 'OVERDUE')"
    )
    long countActiveBookLoansByUser(@Param("userId") Long userId);

    @Query("SELECT COUNT(bl) from BookLoan bl where bl.user.id = :userId " + 
           " AND bl.status = 'OVERDUE')"
    )
    long countOverdueBookLoansByUser(@Param("userId") Long userId);

    @Query("SELECT bl from BookLoan bl where bl.dueDate < :currentDate " +
        " AND (bl.status = 'CHECKED_OUT' OR bl.status = 'OVERDUE')"
    )
    Page<BookLoan> findOverdueBookLoans(LocalDate now, Pageable pageable);

    @Query("SELECT bl from BookLoan bl where bl.checkoutDate between :startDate AND :endDate")
    Page<BookLoan> findBookLoansByDateRange(@Param("startDate") LocalDate startDate, 
                                            @Param("endDate") LocalDate endDate, Pageable pageable);
}
