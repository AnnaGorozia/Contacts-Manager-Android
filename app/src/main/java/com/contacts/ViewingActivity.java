package com.contacts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.contacts.adapters.MailAdapter;
import com.contacts.adapters.PhoneNumberAdapter;
import com.contacts.models.Contact;
import com.contacts.models.Mail;
import com.contacts.models.Mobile;
import com.contacts.utils.Utility;

import java.io.FileNotFoundException;
import java.util.ArrayList;



public class ViewingActivity extends AppCompatActivity {

    private Contact user;
    private TextView contactName;

    private ImageView contactImage;
    private ListView phonesView;
    private ArrayList<Mobile> phoneNumbers;
    private PhoneNumberAdapter phoneNumberAdapter;

    private ArrayList<Mail> mails;
    private ListView mailsView;
    private MailAdapter mailAdapter;

    private static Bitmap contactPhoto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_viewing);

        long userId = getIntent().getLongExtra("userID", -1L);
        if (userId != -1) {
            user = Contact.findById(Contact.class, userId);
        }

        contactImage = (ImageView) findViewById(R.id.contact_image);
        if (contactImage != null) {
            if (user.getUri() != null) {
                try {
                    contactPhoto = Utility.decodeUri(Uri.parse(user.getUri()), getContentResolver(), 140);
                    contactImage.setImageBitmap(contactPhoto);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                contactImage.setImageResource(R.mipmap.ic_launcher);
            }
        }
        if (user != null) {
            setTitle("");
            contactName = (TextView)findViewById(R.id.contact_name);
            contactName.setText(user.getName());
            phoneNumbers = (ArrayList<Mobile>) Mobile.find(Mobile.class, "contact = ?", "" + user.getId());
            phonesView = (ListView) findViewById(R.id.phones);
            if (phoneNumbers != null) {
                phoneNumberAdapter = new PhoneNumberAdapter(this, R.layout.phone_numbers_list, phoneNumbers, false);
                if (phonesView != null) {
                    phonesView.setAdapter(phoneNumberAdapter);
                }
            }
            mails = (ArrayList<Mail>) Mail.find(Mail.class, "contact = ?", "" + user.getId());
            mailsView = (ListView) findViewById(R.id.mails);
            if (mails != null) {
                mailAdapter = new MailAdapter(this, R.layout.emails_list, mails, false);
                if (mailsView != null) {
                    mailsView.setAdapter(mailAdapter);
                }
            }
        }


        ImageView edit = (ImageView) findViewById(R.id.edit_user);

        if (edit != null) {
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ViewingActivity.this, EditingActivity.class);
                    intent.putExtra("userID", user.getId());
                    startActivityForResult(intent, 1);
                }
            });
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            finish();
        }

        if (resultCode == RESULT_OK) {
            user = Contact.findById(Contact.class, getIntent().getLongExtra("userID", -1L));
            contactName.setText(user.getName());
            setImage();

            refreshPhonesList();
            refreshMailList();
        }
    }

    private void setImage() {
        if (user.getUri() != null) {
            contactImage.setImageBitmap(getContactPhoto());
        } else {
            contactImage.setImageResource(R.mipmap.ic_launcher);
        }
    }

    private void refreshPhonesList() {
        phoneNumbers = (ArrayList<Mobile>) Mobile.find(Mobile.class, "contact = ?", "" + user.getId());
        if (phoneNumbers != null) {
            if (phoneNumberAdapter == null) {
                phoneNumberAdapter = new PhoneNumberAdapter(this, R.layout.phone_numbers_list, phoneNumbers, false);
                phonesView.setAdapter(phoneNumberAdapter);
            } else {
                phonesView.setAdapter(null);
                phoneNumberAdapter.clear();
                phoneNumberAdapter.addAll(phoneNumbers);
                phoneNumberAdapter.notifyDataSetChanged();
                phonesView.setAdapter(phoneNumberAdapter);
            }
        }
    }

    private void refreshMailList() {
        mails = (ArrayList<Mail>) Mail.find(Mail.class, "contact = ?", "" + user.getId());
        if (mails != null) {
            if (mailAdapter == null) {
                mailAdapter = new MailAdapter(this, R.layout.emails_list, mails, false);
                mailsView.setAdapter(mailAdapter);
            } else {
                mailsView.setAdapter(null);
                mailAdapter.clear();
                mailAdapter.addAll(mails);
                mailAdapter.notifyDataSetChanged();
                mailsView.setAdapter(mailAdapter);
            }
        }
    }

    public static Bitmap getContactPhoto() {
        return contactPhoto;
    }

    public static void setContactPhoto(Bitmap contactPhoto) {
        ViewingActivity.contactPhoto = contactPhoto;
    }

}