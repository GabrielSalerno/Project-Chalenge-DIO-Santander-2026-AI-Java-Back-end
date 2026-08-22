package dio.budgeting.infrastructure.http.response;

import dio.budgeting.application.output.DeleteTransactionOutput;

public record DeleteResponse(String description) {
    public static DeleteResponse from(DeleteTransactionOutput output){
        return new DeleteResponse(output.description());
    }
}
