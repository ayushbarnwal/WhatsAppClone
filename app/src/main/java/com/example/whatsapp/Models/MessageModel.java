package com.example.whatsapp.Models;

public class MessageModel {

    String uId , message,messageId,photoUrl;
    Long timestamp;

    public MessageModel(String uId, String message, Long timestamp) {
        this.message = message;
        this.uId = uId;
        this.timestamp = timestamp;
    }

    public MessageModel(String uId, String message) {
        this.message = message;
        this.uId = uId;
    }

    public MessageModel(String uId, String photoUrl,long timestamp) {
        this.uId = uId;
        this.photoUrl = photoUrl;
        this.timestamp=timestamp;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public MessageModel(){}

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
