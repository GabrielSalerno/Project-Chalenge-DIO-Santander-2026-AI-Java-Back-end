package dio.budgeting.application;

import dio.budgeting.application.output.TransactionOutput;
import dio.budgeting.domain.TransactionRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class ListMonthTransactionsUseCase {
    private final TransactionRepository transactionRepository;

    public ListMonthTransactionsUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Tool(name = "list-month-transactions",description = "Busca todas as transações do mês informado pelo usuário")
    public List<TransactionOutput> execute(Integer month, Integer year){
        LocalDate today = LocalDate.now();

        int selectedMonth = (month != null) ? month : today.getMonthValue();
        int selectedYear = (year != null) ? year : today.getYear();

        YearMonth yearMonth = YearMonth.of(selectedYear, selectedMonth);

        Instant firstDayOfTheMonth = yearMonth.atDay(1)
                .atStartOfDay()
                .toInstant(ZoneOffset.UTC);

        Instant lastDayOfTheMonth = yearMonth.atEndOfMonth()
                .atTime(23,59,59)
                .toInstant(ZoneOffset.UTC);

        return transactionRepository.findMonthTransactions(firstDayOfTheMonth, lastDayOfTheMonth)
                .stream()
                .map(TransactionOutput::from)
                .toList();
    }
}
