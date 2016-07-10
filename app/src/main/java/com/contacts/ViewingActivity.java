package com.contacts;

import android.content.Intent;
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
import com.klinker.android.sliding.SlidingActivity;

import java.util.ArrayList;



public class ViewingActivity extends AppCompatActivity {

    private Contact user;
    TextView contactName;

    ListView phonesView;
    ArrayList<Mobile> phoneNumbers;
    PhoneNumberAdapter phoneNumberAdapter;

    ArrayList<Mail> mails;
    ListView mailsView;
    MailAdapter mailAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_viewing);

        long userId = getIntent().getLongExtra("userID", -1L);
        if (userId != -1) {
            user = Contact.findById(Contact.class, userId);
        }

        ImageView contactImage = (ImageView) findViewById(R.id.contact_image);
        if (contactImage != null) {
            contactImage.setImageResource(R.mipmap.ic_launcher);
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
            refreshPhonesList();
            refreshMailList();
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



}