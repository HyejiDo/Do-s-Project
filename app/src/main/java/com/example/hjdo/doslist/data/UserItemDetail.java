package com.example.hjdo.doslist.data;


import java.io.Serializable;

public class UserItemDetail implements Serializable{
    String login;
    String avatar_url;
    String html_url;
    String name;
    String email;
    String location;

    public UserItemDetail (String login, String html_url, String name, String email, String location, String avatar_url){
        this.login = login;
        this.html_url = html_url;
        this.name = name;
        this.email = email;
        this.location = location;
        this.avatar_url = avatar_url;
    }

    public String getLogin() { return login; }

    public void setLogin(String login) { this.login = login; }

    public String getAvatar_url() { return avatar_url; }

    public void setAvatar_url(String avatar_url) { this.avatar_url = avatar_url; }

    public String getHtml_url() { return html_url; }

    public void setHtml_url(String html_url) { this.html_url = html_url; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }
}
