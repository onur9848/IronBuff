package com.senerunosoft.ironbuff.table;

import java.util.List;

public class MessageListTable {

    private String uuid;
    private List<String> users;

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
}
