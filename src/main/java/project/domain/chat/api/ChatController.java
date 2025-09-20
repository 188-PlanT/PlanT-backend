package project.domain.chat.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.domain.chat.dto.request.ChatCreateRequest;
import project.domain.chat.dto.request.ChatUpdateRequest;
import project.domain.chat.service.ChatService;

@Tag(name = "6. [Chat]", description = "댓글 API")
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @Deprecated
    @Operation(summary = "댓글 추가", description = "스케줄에 댓글을 추가합니다.")
    @PostMapping("/v1/chats")
    public ResponseEntity<Void> createChat(@RequestBody @Valid ChatCreateRequest request) {
        chatService.createChat(request);
        return ResponseEntity.ok().build();
    }

    @Deprecated
    @Operation(summary = "댓글 수정", description = "댓글을 수정합니다.")
    @PutMapping("/v1/chats/{chatId}")
    public ResponseEntity<Void> updateChat(@PathVariable Long chatId, @Valid @RequestBody ChatUpdateRequest request) {
        chatService.updateChat(chatId, request);
        return ResponseEntity.ok().build();
    }

    @Deprecated
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    @DeleteMapping("/v1/chats/{chatId}")
    public ResponseEntity<Void> deleteChat(@PathVariable Long chatId) {
        chatService.deleteChat(chatId);
        return ResponseEntity.ok().build();
    }
}
