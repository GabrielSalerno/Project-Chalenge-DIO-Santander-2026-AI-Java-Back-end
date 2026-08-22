package dio.budgeting.application;

import dio.budgeting.application.output.TotalSumOutput;
import dio.budgeting.domain.TransactionRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class TotalSumUseCase {
    private final TransactionRepository transactionRepository;

    public TotalSumUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Tool(name = "total-sum",description = "Soma total de gastos do usuário")
    public TotalSumOutput execute(){
        Long sum = transactionRepository.totalSum();

        if(sum == null){
            sum = 0L;
        }

        return new TotalSumOutput(sum);
    }
}
