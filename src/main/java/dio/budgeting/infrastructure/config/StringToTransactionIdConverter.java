package dio.budgeting.infrastructure.config;

import dio.budgeting.domain.TransactionId;
import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class StringToTransactionIdConverter implements Converter<String, TransactionId> {
    @Override
    public TransactionId convert(@NonNull String source) {
        return new TransactionId(UUID.fromString(source));
    }
}
