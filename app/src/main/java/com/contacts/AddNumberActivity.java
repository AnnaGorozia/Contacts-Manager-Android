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
import com.contacts.models.Mobile;

public class AddNumberActivity extends AppCompatActivity {
    Contact user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_number);

        init();

        long userId = getIntent().getLongExtra("userID", -1L);
        if (userId != -1) {
            user = Contact.findById(Contact.class, userId);
        }

        final EditText number = (EditText) findViewById(R.id.new_number);

        ImageView approve = (ImageView) findViewById(R.id.approve_number);
        if (approve != null) {
            approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newNum = number.getText().toString();
                    Mobile mobile = new Mobile(user, newNum);
                    mobile.save();
                    Intent returnIntent = new Intent();
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            });
        }
    }

    private void init() {
        setTitle("New Number");
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }
}
