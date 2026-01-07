package ru.bsuedu.cad.sem2.lab1;

import java.time.LocalDateTime;

public class Message {

    private String message;
    private LocalDateTime creationDate;
    
    public Message() {
        message = new String();
        creationDate = LocalDateTime.now();
    }
    public Message(String message) {
        this.message = message;
        creationDate = LocalDateTime.now();
    }
    public Message(String message, LocalDateTime creationDate) {
        this.message = message;
        this.creationDate = creationDate;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public LocalDateTime getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((message == null) ? 0 : message.hashCode());
        result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Message other = (Message) obj;
        if (message == null) {
            if (other.message != null)
                return false;
        } else if (!message.equals(other.message))
            return false;
        if (creationDate == null) {
            if (other.creationDate != null)
                return false;
        } else if (!creationDate.equals(other.creationDate))
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "UserMessage [message=" + message + ", creationDate=" + creationDate + "]";
    }
}
