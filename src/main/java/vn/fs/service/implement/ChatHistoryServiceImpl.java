package vn.fs.service.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.fs.entity.ChatHistory;
import vn.fs.entity.User;
import vn.fs.repository.ChatHistoryRepository;
import vn.fs.service.ChatHistoryService;
import vn.fs.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatHistoryServiceImpl implements ChatHistoryService {

    private final ChatHistoryRepository chatHistoryRepository;
    private final UserService userService;

    @Autowired
    public ChatHistoryServiceImpl(ChatHistoryRepository chatHistoryRepository, UserService userService) {
        this.chatHistoryRepository = chatHistoryRepository;
        this.userService = userService;
    }

    @Override
    public void saveChatHistory(ChatHistory chatHistory) {
        User currentUser = userService.getCurrentUser();
        chatHistory.setUser(currentUser);
        chatHistory.setTimestamp(LocalDateTime.now());
        chatHistoryRepository.save(chatHistory);
    }

    @Override
    public List<ChatHistory> getChatHistoriesByUser(User user) {
        return chatHistoryRepository.findByUser(user);
    }
}