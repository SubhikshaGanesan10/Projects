package edu.uga.cs.hungerhero;

public class ChatMessage {
    private String messageId;
    private String messageText;
    private String senderId;

    public ChatMessage() {
        // Default constructor required for Firebase
    }

    public ChatMessage(String messageId, String messageText, String senderId) {
        this.messageId = messageId;
        this.messageText = messageText;
        this.senderId = senderId;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getSenderId() {
        return senderId;
    }
}