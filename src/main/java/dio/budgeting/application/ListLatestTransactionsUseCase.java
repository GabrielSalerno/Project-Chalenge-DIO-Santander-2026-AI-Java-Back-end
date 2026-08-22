package dio.budgeting.application;

import dio.budgeting.application.output.TransactionOutput;
import dio.budgeting.domain.TransactionRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListLatestTransactionsUseCase {
    private final TransactionRepository transactionRepository;

    public ListLatestTransactionsUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Tool(name = "list-latest-transactions",description = "Busca as últimas 10 transações do usuário")
    public List<TransactionOutput> execute(){
        return transactionRepository.findLatestTransactions().stream().map(TransactionOutput::from).toList();
    }
}
