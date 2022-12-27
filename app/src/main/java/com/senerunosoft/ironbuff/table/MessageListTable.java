package com.senerunosoft.ironbuff.table;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageListTable implements Comparable<MessageListTable> {

    private String uuid;
    private List<String> users;
    private String lastMessage;
    private Date lastMessageTime;
    private List<String> userName;
    private List<String> userImg;

    public MessageListTable() {
        users = new ArrayList<>();
        userName = new ArrayList<>();
        userImg = new ArrayList<>();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }


    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Date getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(Date lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public List<String> getUserName() {
        return userName;
    }

    public void setUserName(List<String> userName) {
        this.userName = userName;
    }

    public List<String> getUserImg() {
        return userImg;
    }

    public void setUserImg(List<String> userImg) {
        this.userImg = userImg;
    }

    @Override
    public int compareTo(MessageListTable messageListTable) {
        if (getLastMessageTime() == null || messageListTable.getLastMessageTime() == null){
            return 0;}

        return getLastMessageTime().compareTo(messageListTable.getLastMessageTime());
    }
}
