package dio.budgeting.infrastructure.http.response;

import dio.budgeting.application.output.TotalSumOutput;

public record TotalSumResponse(Long totalAmount) {
    public static TotalSumResponse from(TotalSumOutput outputSum){
        return new TotalSumResponse(outputSum.totalAmount());
    }
}
