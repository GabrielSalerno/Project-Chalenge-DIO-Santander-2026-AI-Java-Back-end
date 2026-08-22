package dio.budgeting.application;

import dio.budgeting.application.output.DeleteTransactionOutput;

import dio.budgeting.domain.TransactionId;
import dio.budgeting.domain.TransactionRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class DeleteTransactionUseCase {
    private final TransactionRepository transactionRepository;

    public DeleteTransactionUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Tool(name = "deleteById-transaction", description = "Deleta uma transação financeira existente pelo ID")
    public DeleteTransactionOutput execute(TransactionId transactionId){
        var transaction = transactionRepository.findById(transactionId)
                                                .orElseThrow(() -> new IllegalArgumentException("Transação não encontrada."));
        transactionRepository.deleteById(transactionId);

        return DeleteTransactionOutput.from(transaction);
    }
}
