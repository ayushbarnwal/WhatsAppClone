package com.example.whatsapp.Models;

public class User {

    private String profilePicture,userName,emailAddress,passsword,lastMessage,userId,status,phoneNo,groupName;

    public User(String profilePicture,String userName,String emaiAddress,String passsword,String lastMessage,String userId) {
        this.userId = userId;
        this.profilePicture = profilePicture;
        this.userName = userName;
        this.emailAddress = emailAddress;
        this.passsword = passsword;
        this.lastMessage = lastMessage;
    }

    public User(){}

    public User(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public User(String userName, String emaiAddress, String passsword) {
        this.userName = userName;
        this.emailAddress = emaiAddress;
        this.passsword = passsword;
    }

    public String getGroupName() {

        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emaiAddress) {
        this.emailAddress = emailAddress;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasssword() {
        return passsword;
    }

    public void setPasssword(String passsword) {
        this.passsword = passsword;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
