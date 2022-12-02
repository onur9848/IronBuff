package com.senerunosoft.ironbuff.table;

import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Date;

public class MessageTable implements Comparable<MessageTable> {

    private int ViewType;
    private String sendMessageByUser;
    private String getMessageByUser;
    private String message;
    private String messageClock;
    private Date messageDate;


    public int getViewType() {
        return ViewType;
    }

    public void setViewType(int viewType) {
        ViewType = viewType;
    }

    public String getSendMessageByUser() {
        return sendMessageByUser;
    }

    public void setSendMessageByUser(String sendMessageByUser) {
        this.sendMessageByUser = sendMessageByUser;
    }

    public String getGetMessageByUser() {
        return getMessageByUser;
    }

    public void setGetMessageByUser(String getMessageByUser) {
        this.getMessageByUser = getMessageByUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageClock() {
        return messageClock;
    }

    public void setMessageClock(String messageClock) {
        this.messageClock = messageClock;
    }

    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }

    @Override
    public int compareTo(MessageTable table) {
        if (getMessageDate() == null || table.getMessageDate()==null){
            return 0;
        }
        return getMessageDate().compareTo(table.getMessageDate());
    }


}
