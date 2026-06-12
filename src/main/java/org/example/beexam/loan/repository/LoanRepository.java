package org.example.beexam.loan.repository;

import org.example.beexam.loan.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    boolean existsByUserIdAndBookIdAndReturnDateIsNull(Long userId, Long bookId);
}