package com.example.hjdo.doslist;

public class UserItem {

    String login;
    String type;
    String avatar_url;

    public UserItem(String login, String type, String avatar_url) {
        this.login = login;
        this.type = type;
        this.avatar_url = avatar_url;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }
}
