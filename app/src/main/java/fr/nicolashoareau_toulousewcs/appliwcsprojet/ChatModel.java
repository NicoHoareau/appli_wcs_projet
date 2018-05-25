package fr.nicolashoareau_toulousewcs.appliwcsprojet;

import com.google.firebase.auth.FirebaseUser;

import java.util.Date;

class ChatModel {
    private String mText;
    private String mSender;
    private Date mDate;

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public String getSender() {
        return mSender;
    }

    public void setSender(String sender) {
        mSender = sender;
    }
}
