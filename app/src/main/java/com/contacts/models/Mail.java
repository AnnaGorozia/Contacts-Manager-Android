package com.contacts.models;

import com.orm.SugarRecord;

public class Mail extends SugarRecord {
    Contact contact;

    String name;

    public Mail(String mail) {
        this.name = mail;
    }

    public Mail(Contact contact, String name) {
        this.contact = contact;
        this.name = name;
    }

    public Mail() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
