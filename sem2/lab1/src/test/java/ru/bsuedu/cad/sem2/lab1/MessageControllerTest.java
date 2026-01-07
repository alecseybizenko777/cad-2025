package ru.bsuedu.cad.sem2.lab1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.*;

@WebMvcTest(MessageController.class)
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // В @WebMvcTest WebApplicationContext не обязателен, т.к. MockMvc уже внедрён
    // Но если нужно — можно оставить, но обычно не требуется

    // Для @WebMvcTest достаточно @Autowired MockMvc — он уже настроен

    // === Тест: корневой маршрут ===

    @BeforeEach
    void resetControllerStateBeforeEachTest() throws Exception {
        // Очистка всех сообщений
        mockMvc.perform(delete("/messages"));

        // Сброс лимита на количество сообщений
        mockMvc.perform(put("/messages/limit")
            .contentType(MediaType.APPLICATION_JSON)
            .content("0"));
    }

    @Test
    void helloWorld_ShouldReturnHelloWorld() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(content().string("Hello, World!"));
    }

    // === Тест: публикация сообщения ===
    @Test
    void publishMessage_WhenValidMessage_ShouldAddAndReturnSuccess() throws Exception {
        mockMvc.perform(post("/messages")
            .contentType(MediaType.APPLICATION_JSON).content("Hello"))
            .andExpect(status().isOk())
            .andExpect(content().string("Message published successfully!"));

        mockMvc.perform(get("/messages"))
            .andExpect(jsonPath("$[0]").value("Hello"));
    }

    @Test
    void publishMessage_WhenWhitespaceMessage_ShouldReturnError() throws Exception {
        mockMvc.perform(post("/messages")
            .contentType(MediaType.APPLICATION_JSON).content("   \n  \t  "))
            .andExpect(status().isOk())
            .andExpect(content().string("Message cannot be empty!"));

        mockMvc.perform(get("/messages"))
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void publishMessage_WhenEmptyMessage_ShouldReturnError() throws Exception {
        mockMvc.perform(post("/messages")
            .contentType(MediaType.APPLICATION_JSON).content(""))
            .andExpect(status().isOk())
            .andExpect(content().string("Message cannot be empty!"));
    }

    // === Тест: ограничение по количеству сообщений ===
    @Test
    void publishMessage_WhenLimitExceeded_ShouldRemoveOldest() throws Exception {
        // Установим лимит = 2
        mockMvc.perform(put("/messages/limit")
            .contentType(MediaType.APPLICATION_JSON).content("2"))
            .andExpect(status().isOk())
            .andExpect(content().string("Messages limit set to 2"));

        // Публикуем 3 сообщения
        mockMvc.perform(post("/messages")
            .contentType(MediaType.APPLICATION_JSON).content("First"));
        mockMvc.perform(post("/messages")
            .contentType(MediaType.APPLICATION_JSON).content("Second"));
        mockMvc.perform(post("/messages")
            .contentType(MediaType.APPLICATION_JSON).content("Third"));

        // Должно остаться только "Second" и "Third"
        mockMvc.perform(get("/messages"))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0]").value("Second"))
            .andExpect(jsonPath("$[1]").value("Third"))
            .andExpect(jsonPath("$", hasSize(2)));
    }

    // === Тест: установка лимита ===
    @Test
    void setMessagesLimit_WhenNegative_ShouldReturnError() throws Exception {
        mockMvc.perform(put("/messages/limit")
            .contentType(MediaType.APPLICATION_JSON).content("-5"))
            .andExpect(status().isOk())
            .andExpect(content().string("Error: Message limit cannot be negative!"));
    }

    @Test
    void getMessagesLimit_ShouldReturnCurrentLimit() throws Exception {
        mockMvc.perform(get("/messages/limit"))
            .andExpect(status().isOk())
            .andExpect(content().string("Current messages limit: 0"));

        mockMvc.perform(put("/messages/limit")
            .contentType(MediaType.APPLICATION_JSON)
            .content("5"));

        mockMvc.perform(get("/messages/limit"))
            .andExpect(status().isOk())
            .andExpect(content().string("Current messages limit: 5"));
    }

    // === Тест: получение всех сообщений ===
    @Test
    void getAllMessages_WhenNoMessages_ShouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/messages"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getAllMessages_WhenMessagesExist_ShouldReturnAll() throws Exception {
        mockMvc.perform(post("/messages")
            .contentType(MediaType.APPLICATION_JSON).content("Msg1"));
        mockMvc.perform(post("/messages")
            .contentType(MediaType.APPLICATION_JSON).content("Msg2"));

        mockMvc.perform(get("/messages"))
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0]").value("Msg1"))
            .andExpect(jsonPath("$[1]").value("Msg2"));
    }

    // === Тест: получение по индексу ===
    @Test
    void getMessageByIndex_WhenValidIndex_ShouldReturnMessage() throws Exception {
        mockMvc.perform(post("/messages")
            .contentType(MediaType.APPLICATION_JSON).content("Test"));
        mockMvc.perform(get("/messages/0"))
            .andExpect(status().isOk())
            .andExpect(content().string("Test"));
    }

    @Test
    void getMessageByIndex_WhenNegativeIndex_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/messages/-1"))
            .andExpect(status().isOk())
            .andExpect(content().string("Message not found at index -1"));
    }

    @Test
    void getMessageByIndex_WhenIndexTooLarge_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/messages/100"))
            .andExpect(status().isOk())
            .andExpect(content().string("Message not found at index 100"));
    }

    // === Тест: обновление сообщения ===
    @Test
    void updateMessage_WhenValidIndex_ShouldUpdate() throws Exception {
        mockMvc.perform(post("/messages")
            .contentType(MediaType.APPLICATION_JSON).content("Old"));
        mockMvc.perform(put("/messages/0")
            .contentType(MediaType.APPLICATION_JSON).content("New"))
            .andExpect(status().isOk())
            .andExpect(content().string("Message updated successfully!"));

        mockMvc.perform(get("/messages/0"))
            .andExpect(content().string("New"));
    }

    @Test
    void updateMessage_WhenInvalidIndex_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(put("/messages/0")
            .contentType(MediaType.APPLICATION_JSON).content("Msg"))
            .andExpect(status().isOk())
            .andExpect(content().string("Message not found at index 0"));
    }

    // === Тест: удаление по индексу ===
    @Test
    void deleteMessage_WhenValidIndex_ShouldRemove() throws Exception {
        mockMvc.perform(post("/messages")
            .contentType(MediaType.APPLICATION_JSON).content("To delete"));
        mockMvc.perform(delete("/messages/0"))
            .andExpect(status().isOk())
            .andExpect(content().string("Message deleted successfully!"));

        mockMvc.perform(get("/messages"))
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void deleteMessage_WhenInvalidIndex_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/messages/10"))
            .andExpect(status().isOk())
            .andExpect(content().string("Message not found at index 10"));
    }

    // === Тест: очистка всех сообщений ===
    @Test
    void clearAllMessages_ShouldClearList() throws Exception {
        mockMvc.perform(post("/messages")
            .contentType(MediaType.APPLICATION_JSON).content("Msg1"));
        mockMvc.perform(post("/messages")
            .contentType(MediaType.APPLICATION_JSON).content("Msg2"));

        mockMvc.perform(delete("/messages"))
            .andExpect(status().isOk())
            .andExpect(content().string("All messages cleared successfully!"));

        mockMvc.perform(get("/messages"))
            .andExpect(jsonPath("$").isEmpty());
    }

    // === Тест: фильтрация по дате ===
    @Test
    void getMessagesAfter_WhenDateInPast_ShouldReturnRecentMessages() throws Exception {

        mockMvc.perform(post("/messages")
            .contentType(MediaType.APPLICATION_JSON).content("Old"));

        Thread.sleep(1000); // гарантируем разницу во времени

        LocalDateTime date = LocalDateTime.now();
        mockMvc.perform(post("/messages")
            .contentType(MediaType.APPLICATION_JSON).content("New"));

        // Формат: yyyy-MM-dd'T'HH:mm:ss
        String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

        mockMvc.perform(get("/messages/after/" + formattedDate))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0]").value("New"))
            .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getMessagesAfter_WhenDateInFuture_ShouldReturnEmptyList() throws Exception {
        LocalDateTime future = LocalDateTime.now().plusDays(1);
        String formattedDate = future.format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        mockMvc.perform(get("/messages/after/" + formattedDate))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getMessagesAfter_WhenNoMessages_ShouldReturnEmptyList() throws Exception {
        LocalDateTime past = LocalDateTime.now().minusHours(1);
        String formattedDate = past.format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        mockMvc.perform(get("/messages/after/" + formattedDate))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getMessagesAfter_WhenIncorrectDate_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/messages/after/2025-13-46T14:22:00"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void getMessageCount_WhenMessagesExist_ShouldReturnCount() throws Exception {
        mockMvc.perform(post("/messages")
            .contentType(MediaType.APPLICATION_JSON).content("message 1"));
        mockMvc.perform(post("/messages")
            .contentType(MediaType.APPLICATION_JSON).content("message 2"));
        
        mockMvc.perform(get("/messages/count"))
            .andExpect(status().isOk())
            .andExpect(content().string("Current messages count: 2"));
    }

    @Test
    void getMessageCount_WhenNoMessages_ShouldReturnZero() throws Exception {
        mockMvc.perform(get("/messages/count"))
            .andExpect(status().isOk())
            .andExpect(content().string("Current messages count: 0"));
    }
}
