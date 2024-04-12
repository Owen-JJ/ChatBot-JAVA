package vn.fs.service.implement;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.cloud.dialogflow.v2.*;
import com.google.auth.oauth2.ServiceAccountCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.fs.entity.ChatHistory;
import vn.fs.entity.User;
import vn.fs.repository.ChatHistoryRepository;
import vn.fs.service.DialogFlowService;
import vn.fs.service.UserService;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class DialogFlowServiceImpl implements DialogFlowService {

    private final static String projectId = "book-bytg";

    private final ChatHistoryRepository chatHistoryRepository;
    private final UserService userService;

    @Autowired
    public DialogFlowServiceImpl(ChatHistoryRepository chatHistoryRepository, UserService userService) {
        this.chatHistoryRepository = chatHistoryRepository;
        this.userService = userService;
    }

    @Override
    public QueryResult runIntent(String requestText) throws Exception {
        String sessionId = UUID.randomUUID().toString();
        SessionsSettings settings = SessionsSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider
                        .create(ServiceAccountCredentials
                                .fromStream(getServiceAccountStream()))).build();
        SessionsClient sessionsClient = SessionsClient.create(settings);

        SessionName session = SessionName.of(projectId, sessionId);
        TextInput.Builder textInput = TextInput.newBuilder().setText(requestText).setLanguageCode("vi");
        QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();

        DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);

        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            saveChatHistory(requestText, response.getQueryResult()
                    .getFulfillmentText(), LocalDateTime.now(), currentUser);
        }

        return response.getQueryResult();
    }

    private InputStream getServiceAccountStream() throws Exception {
        return Files.newInputStream(Paths.get("book-bytg-3cc03e33cc70.json"));
    }

    private void saveChatHistory(String message, String response, LocalDateTime timestamp, User user) {
        ChatHistory chatHistory = new ChatHistory();
        chatHistory.setMessage(message);
        chatHistory.setResponse(response);
        chatHistory.setTimestamp(timestamp);
        chatHistory.setUser(user);

        chatHistoryRepository.save(chatHistory);
    }


}
