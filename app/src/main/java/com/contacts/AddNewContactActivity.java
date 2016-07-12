package com.contacts;

import android.content.Intent;
import android.net.Uri;
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
import com.contacts.utils.Utility;

import java.io.FileNotFoundException;

public class AddNewContactActivity extends AppCompatActivity {

    private static final int SELECT_PHOTO = 100;
    private EditText mail;
    private EditText number;
    private EditText name;
    private ImageView image;
    private ImageView upload;
    private String uri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_contact);

        init();

        mail = (EditText) findViewById(R.id.new_contact_mail);
        number = (EditText) findViewById(R.id.new_contact_number);
        name = (EditText) findViewById(R.id.new_contact_name);
        image = (ImageView) findViewById(R.id.new_contact_image);
        upload = (ImageView) findViewById(R.id.new_upload);

        image.setImageResource(R.mipmap.ic_launcher);

        ImageView approve = (ImageView) findViewById(R.id.approve_contact);
        if (approve != null) {
            approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Contact contact = new Contact(name.getText().toString(), uri);
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

        if (upload != null) {
            upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO);
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PHOTO) {
            if(resultCode == RESULT_OK){
                Uri selectedImage = data.getData();
                uri = selectedImage.toString();
                try {
                    image.setImageBitmap(Utility.decodeUri(Uri.parse(uri), getContentResolver(), 140));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
    }

}
