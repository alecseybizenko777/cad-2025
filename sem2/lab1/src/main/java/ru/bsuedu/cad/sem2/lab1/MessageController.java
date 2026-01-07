package ru.bsuedu.cad.sem2.lab1;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MessageController {
    // private List<String> userMessages = new LinkedList<>();
    private List<Message> userMessages = new LinkedList<>();
    private int limitMessages = 0;

    @GetMapping("/")
    public String helloWorld() {
        return "Hello, World!";
    }

    @GetMapping("/messages")
    public List<String> getAllMessages() {
        return userMessages.stream()
            .map(Message::getMessage)
            .collect(Collectors.toList());
    }

    // Публикует сообщение
    @PostMapping("/messages")
    public String publishMessage(@RequestBody(required = false) String message) {
        if (message == null || message.trim().isEmpty()) {
            return "Message cannot be empty!";
        }
        userMessages.add(new Message(message.trim()));

        if (limitMessages > 0) {
            while (userMessages.size() > limitMessages) {
                ((LinkedList<Message>) userMessages).remove();
            }
        }
        return "Message published successfully!";
    }

    @PutMapping("/messages/{index}")
    public String updateMessage(@PathVariable("index") int index, @RequestBody(required = false) String message) {
        if (message == null || message.trim().isEmpty()) {
            return "Message cannot be empty!";
        }
        if (index < 0 || index >= userMessages.size()) {
            return "Message not found at index " + index;
        }
        userMessages.set(index, new Message(message.trim()));
        return "Message updated successfully!";
    }

    @DeleteMapping("/messages/{index}")
    public String deleteMessage(@PathVariable("index") int index) {
        if (index >= 0 && index < userMessages.size()) {
            userMessages.remove(index);
            return "Message deleted successfully!";
        }
        return "Message not found at index " + index;
    }


    // Очищает список сообщений
    @DeleteMapping("/messages")
    public String clearAllMessages() {
        userMessages.clear();
        return "All messages cleared successfully!";
    }

    // Возвращает сообщение по индексу
    @GetMapping("/messages/{index}")
    public String getMessageByIndex(@PathVariable("index") int index) {
        if (index >= 0 && index < userMessages.size()) {
            return userMessages.get(index).getMessage();
        }
        return "Message not found at index " + index;
    }

    // Возвращает текущий лимит сообщений
    @GetMapping("/messages/limit")
    public String getMessagesLimit() {
        return "Current messages limit: " + limitMessages;
    }

    // Устанавливает новый лимит сообщений
    @PutMapping("/messages/limit")
    public String setMessagesLimit(@RequestBody int limit) {
        if (limit < 0) {
            return "Error: Message limit cannot be negative!";
        }
        this.limitMessages = limit;

        while (userMessages.size() > limitMessages) {
            ((LinkedList<Message>) userMessages).removeFirst();
        }

        return "Messages limit set to " + limitMessages;
    }

    // Возвращает все сообщения опубликованные после указанной даты
    // Дату необходимо указывать в формате yyyy-MM-ddTHH-mm-ss
    // Например, если необходимо получить все сообщения после 12 февраля 2025 14:21
    // то необходимо сделать GET запрос по адресу /messages/after/2025-02-12T14:21:00
    @GetMapping("/messages/after/{date}")
    public List<String> getMessagesAfter(@PathVariable("date") LocalDateTime date) {
        return userMessages.stream()
            .filter(message -> message.getCreationDate().isAfter(date))
            .map(Message::getMessage)
            .collect(Collectors.toList());
    }

    // Возвращает количество сообщений
    @GetMapping("/messages/count")
    public String getMessagesCount() {
        return "Current messages count: " + userMessages.size();
    }
}