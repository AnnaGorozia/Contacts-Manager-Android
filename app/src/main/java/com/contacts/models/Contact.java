package com.contacts.models;

import com.orm.SugarRecord;

import java.util.List;

public class Contact extends SugarRecord{

    String name;

    public Contact() {

    }

    public Contact(String name) {
        this.name = name;
    }


    public List<Mobile> getMobiles() {
        return Mobile.find(Mobile.class, "contact = ?", "" + getId());
    }


    List<Mail> getMails() {
        return Mail.find(Mail.class, "contact = ?", "" + getId());
    }

    @Override
    public String toString() {
        return name + "mails: " + getMails().toString() + "\nmobiles: " + getMobiles().toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
