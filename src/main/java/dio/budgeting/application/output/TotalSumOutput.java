package dio.budgeting.application.output;

public record TotalSumOutput(Long totalAmount) {
    public static TotalSumOutput from(Long sum){
        return new TotalSumOutput(sum);
    }
}
