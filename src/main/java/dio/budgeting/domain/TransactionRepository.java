package dio.budgeting.domain;

import jakarta.persistence.criteria.CriteriaBuilder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository {
    Transaction save(Transaction transaction);

    List<Transaction> findAllByCategory(Category category);

    Optional<Transaction> findById(TransactionId transactionId);

    void deleteById(TransactionId transactionId);

    List<Transaction> findLatestTransactions();

    Long totalSum();

    List<Transaction> findMonthTransactions(Instant createdOnAfter, Instant createdOnBefore);
}