package com.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.contacts.models.Contact;
import com.contacts.models.Mail;
import com.contacts.models.Mobile;

public class AddNewContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_contact);

        init();

        final EditText mail = (EditText) findViewById(R.id.new_contact_mail);
        final EditText number = (EditText) findViewById(R.id.new_contact_number);
        final EditText name = (EditText) findViewById(R.id.new_contact_name);

        ImageView approve = (ImageView) findViewById(R.id.approve_contact);
        if (approve != null) {
            approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Contact contact = new Contact(name.getText().toString());
                    contact.save();

                    String newNum = number.getText().toString();

                    if (newNum != null || newNum.length() != 0) {
                        Mobile mobile = new Mobile(contact, newNum);
                        mobile.save();
                    }

                    String newMail = mail.getText().toString();
                    if (newMail != null || newMail.length() != 0) {
                        Mail mail = new Mail(contact, newMail);
                        mail.save();
                    }

                    Intent returnIntent = new Intent();
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            });
        }
    }

    private void init() {
        setTitle("New Contact");
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

}
