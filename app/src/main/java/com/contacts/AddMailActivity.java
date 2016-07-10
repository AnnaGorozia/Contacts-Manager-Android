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

public class AddMailActivity extends AppCompatActivity {

    Contact user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_mail);

        init();

        long userId = getIntent().getLongExtra("userID", -1L);
        if (userId != -1) {
            user = Contact.findById(Contact.class, userId);
        }

        final EditText mail = (EditText) findViewById(R.id.new_mail);

        ImageView approve = (ImageView) findViewById(R.id.approve_mail);
        if (approve != null) {
            approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newNum = mail.getText().toString();
                    Mail mail = new Mail(user, newNum);
                    mail.save();
                    Intent returnIntent = new Intent();
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            });
        }
    }

    private void init() {
        setTitle("New Mail");
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }
}
