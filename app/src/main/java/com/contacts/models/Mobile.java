package com.contacts.models;

import com.orm.SugarRecord;

public class Mobile extends SugarRecord {

    Contact contact;
    String name;

    public Mobile() {
    }

    public Mobile(Contact contact, String name) {
        this.contact = contact;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String mobile) {
        this.name = mobile;
    }
}
