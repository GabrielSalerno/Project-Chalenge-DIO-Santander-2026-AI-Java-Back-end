package dio.budgeting.infrastructure.persistence.repository;

import dio.budgeting.domain.Category;
import dio.budgeting.domain.Transaction;
import dio.budgeting.domain.TransactionId;
import dio.budgeting.domain.TransactionRepository;
import dio.budgeting.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaTransactionRepository implements TransactionRepository {
    private final TransactionEntityRepository transactionEntityRepository;

    public JpaTransactionRepository(TransactionEntityRepository transactionEntityRepository) {
        this.transactionEntityRepository = transactionEntityRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        var entity = TransactionEntity.from(transaction);
        return transactionEntityRepository.save(entity).toDomain();
    }

    @Override
    public List<Transaction> findAllByCategory(Category category) {
        return transactionEntityRepository.findAllByCategory(category)
                .stream()
                .map(TransactionEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<Transaction> findById(TransactionId transactionId) {
        UUID id = transactionId.uuid();

        return transactionEntityRepository.findById(id)
                .map(TransactionEntity::toDomain);
    }

    @Override
    public void deleteById(TransactionId transactionId) {
        UUID id = transactionId.uuid();
        transactionEntityRepository.deleteById(id);
    }

    @Override
    public List<Transaction> findLatestTransactions() {
        return transactionEntityRepository.findTop10ByOrderByCreatedOnDesc()
                .stream()
                .map(TransactionEntity::toDomain)
                .toList();
    }

    @Override
    public Long totalSum() {
        return transactionEntityRepository.totalSum();
    }

    @Override
    public List<Transaction> findMonthTransactions(Instant createdOnAfter, Instant createdOnBefore) {
        return transactionEntityRepository.findByCreatedOnBetween(createdOnAfter, createdOnBefore)
                .stream()
                .map(TransactionEntity::toDomain)
                .toList();
    }
}