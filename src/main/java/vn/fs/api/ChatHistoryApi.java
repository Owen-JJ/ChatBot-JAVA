package vn.fs.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import vn.fs.entity.ChatHistory;
import vn.fs.repository.ChatHistoryRepository;
import vn.fs.repository.UserRepository;
import vn.fs.service.ChatHistoryService;

import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("api/chatHistory")
public class ChatHistoryApi {

    private final ChatHistoryService chatHistoryService;
    private final ChatHistoryRepository chatHistoryRepository;
    private final UserRepository userRepository;

    @Autowired
    public ChatHistoryApi(ChatHistoryService chatHistoryService, ChatHistoryRepository chatHistoryRepository, UserRepository userRepository) {
        this.chatHistoryService = chatHistoryService;
        this.chatHistoryRepository = chatHistoryRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveChatHistory(@RequestBody ChatHistory chatHistory) {
        try {
            chatHistoryService.saveChatHistory(chatHistory);
            return ResponseEntity.ok("Chat history saved successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save chat history");
        }
    }

    @GetMapping("email/{email}")
    public ResponseEntity<List<ChatHistory>> findByEmail(@PathVariable("email") String email) {
        if (userRepository.existsByEmail(email)) {
            return ResponseEntity.ok(chatHistoryRepository.findByUser(userRepository.findByEmail(email).get()));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<ChatHistory>> findByChatHistory(@PathVariable("id") Long id) {
        return ResponseEntity.ok(chatHistoryRepository.findById(id));
    }

    @PostMapping("email")
    public ResponseEntity<ChatHistory> post(@RequestBody ChatHistory chatHistory) {
        return ResponseEntity.ok(chatHistoryRepository.save(chatHistory));
    }

}