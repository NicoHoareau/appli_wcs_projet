package fr.nicolashoareau_toulousewcs.appliwcsprojet.model;

import java.util.Date;

public class ChatModel {
    private String name;
    private String msg;

    public ChatModel() {
    }

    public ChatModel(String name, String message) {
        this.name = name;
        this.msg = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String message) {
        this.msg = message;
    }
}
