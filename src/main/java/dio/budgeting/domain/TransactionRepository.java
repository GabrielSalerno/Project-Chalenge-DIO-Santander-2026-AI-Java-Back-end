package dio.budgeting.domain;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {
    Transaction save(Transaction transaction);

    List<Transaction> findAllByCategory(Category category);

    Optional<Transaction> findById(TransactionId transactionId);

    void deleteById(TransactionId transactionId);

    List<Transaction> findLatestTransactions();

    Long totalSum();
}