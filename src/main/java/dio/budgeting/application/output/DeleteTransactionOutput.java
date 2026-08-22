package dio.budgeting.application.output;

import dio.budgeting.domain.Transaction;

public record DeleteTransactionOutput(String description) {
    public static DeleteTransactionOutput from(Transaction transaction){
        return new DeleteTransactionOutput(transaction.getDescription());
    }
}
