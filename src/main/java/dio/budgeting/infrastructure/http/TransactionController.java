package dio.budgeting.infrastructure.http;

import dio.budgeting.application.*;
import dio.budgeting.domain.Category;
import dio.budgeting.domain.TransactionId;
import dio.budgeting.infrastructure.http.request.TransactionRequest;
import dio.budgeting.infrastructure.http.response.DeleteResponse;
import dio.budgeting.infrastructure.http.response.TotalSumResponse;
import dio.budgeting.infrastructure.http.response.TransactionResponse;
import org.springframework.ai.audio.transcription.TranscriptionModel;
import org.springframework.ai.audio.tts.TextToSpeechModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final PersistTransactionUseCase persistTransactionUseCase;
    private final ListTransactionsByCategoryUseCase listTransactionsByCategoryUseCase;
    private final DeleteTransactionUseCase deleteTransactionUseCase;
    private final ListLatestTransactionsUseCase listLatestTransactionsUseCase;
    private final TotalSumUseCase totalSumUseCase;
    private final ListMonthTransactionsUseCase listMonthTransactionsUseCase;

    private final TranscriptionModel transcriptionModel;
    private final ChatClient chatClient;
    private final TextToSpeechModel textToSpeechModel;

    public TransactionController(PersistTransactionUseCase persistTransactionUseCase,
                                 ListTransactionsByCategoryUseCase listTransactionsByCategoryUseCase,
                                 DeleteTransactionUseCase deleteTransactionUseCase,
                                 ListLatestTransactionsUseCase listLatestTransactionsUseCase,
                                 TotalSumUseCase totalSumUseCase,
                                 ListMonthTransactionsUseCase listMonthTransactionsUseCase,
                                 TranscriptionModel transcriptionModel,
                                 @Value("classpath:prompts/system-message.st") Resource systemPrompt,
                                 ChatClient.Builder chatClientBuilder,
                                 TextToSpeechModel textToSpeechModel) throws IOException {
        this.persistTransactionUseCase = persistTransactionUseCase;
        this.listTransactionsByCategoryUseCase = listTransactionsByCategoryUseCase;
        this.deleteTransactionUseCase = deleteTransactionUseCase;
        this.listLatestTransactionsUseCase = listLatestTransactionsUseCase;
        this.totalSumUseCase = totalSumUseCase;
        this.listMonthTransactionsUseCase = listMonthTransactionsUseCase;
        this.transcriptionModel = transcriptionModel;
        this.chatClient = chatClientBuilder
                .defaultSystem(systemPrompt.getContentAsString(Charset.defaultCharset()))
                .defaultTools(persistTransactionUseCase, listTransactionsByCategoryUseCase,
                        deleteTransactionUseCase, listLatestTransactionsUseCase,
                        totalSumUseCase, listMonthTransactionsUseCase)
                .build();
        this.textToSpeechModel = textToSpeechModel;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse createTransaction(@RequestBody TransactionRequest request) {
        var transaction = persistTransactionUseCase.execute(request.toInput());
        return TransactionResponse.from(transaction);
    }

    @GetMapping("/{category}")
    public List<TransactionResponse> readTransactions(@PathVariable Category category) {
        return listTransactionsByCategoryUseCase.execute(category).stream().map(TransactionResponse::from).toList();
    }

    @DeleteMapping("/{id}")
    public DeleteResponse deleteTransaction(@PathVariable TransactionId id){
        var transaction = deleteTransactionUseCase.execute(id);
        return DeleteResponse.from(transaction);
    }

    @GetMapping
    public List<TransactionResponse> readLatestTransactions(){
        return listLatestTransactionsUseCase.execute().stream().map(TransactionResponse::from).toList();
    }

    @GetMapping("/total")
    public TotalSumResponse totalSum(){
        var output = totalSumUseCase.execute();
        return TotalSumResponse.from(output);
    }

    @GetMapping("/monthly")
    public  List<TransactionResponse> listMonthTransactions(@RequestParam(required = false) Integer month,
                                                     @RequestParam(required = false) Integer year){
        return listMonthTransactionsUseCase.execute(month, year).stream().map(TransactionResponse::from).toList();
    }

    @PostMapping(value = "/ai", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = "audio/mp3")
    ResponseEntity<Resource> transcribe(@RequestParam("file") MultipartFile file) {
        var userMessage = transcriptionModel.transcribe(file.getResource());
        var result = chatClient.prompt().user(userMessage).call().content();

        byte[] audio = textToSpeechModel.call(result);
        var resource = new ByteArrayResource(audio);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename("audio.mp3")
                                .build()
                                .toString())
                .body(resource);
    }
}