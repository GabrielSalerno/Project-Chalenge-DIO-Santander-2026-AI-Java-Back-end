package dio.budgeting.infrastructure.http.response;

import dio.budgeting.application.output.DeleteTransactionOutput;

public record DeleteResponse(String message) {
    public static DeleteResponse from(DeleteTransactionOutput output){
        return new DeleteResponse(output.message());
    }
}
