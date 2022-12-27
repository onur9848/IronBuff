package com.senerunosoft.ironbuff.table;

public class PostTable {

    ImageTable imageTable;
    String username;
    String userImg;

    public PostTable() {
        imageTable = new ImageTable();
    }

    public ImageTable getImageTable() {
        return imageTable;
    }

    public void setImageTable(ImageTable imageTable) {
        this.imageTable = imageTable;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }
}
