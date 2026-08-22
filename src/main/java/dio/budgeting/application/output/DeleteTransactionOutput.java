package dio.budgeting.application.output;

import dio.budgeting.domain.Transaction;

public record DeleteTransactionOutput(String message) {
    public static DeleteTransactionOutput from(Transaction transaction){
        return new DeleteTransactionOutput("A transação de " + transaction.getDescription() + " foi apagada!");
    }
}
