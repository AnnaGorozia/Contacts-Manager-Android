package com.contacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.contacts.adapters.MailAdapter;
import com.contacts.adapters.PhoneNumberAdapter;
import com.contacts.models.Contact;
import com.contacts.models.Mail;
import com.contacts.models.Mobile;

import java.util.ArrayList;

public class EditingActivity extends Activity {

    private Contact user;
    EditText contactName;

    ListView phonesView;
    ArrayList<Mobile> phoneNumbers;
    PhoneNumberAdapter phoneNumberAdapter;

    ArrayList<Mail> mails;
    ListView mailsView;
    MailAdapter mailAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.action_editing);

        long userId = getIntent().getLongExtra("userID", -1L);
        if (userId != -1) {
            user = Contact.findById(Contact.class, userId);
        }

        final ImageView contactImage = (ImageView) findViewById(R.id.contact_image);
        if (contactImage != null) {
            contactImage.setImageResource(R.mipmap.ic_launcher);
        }
        if (user != null) {
            setTitle("");
            contactName = (EditText) findViewById(R.id.contact_name);
            contactName.setText(user.getName());
            phoneNumbers = (ArrayList<Mobile>) Mobile.find(Mobile.class, "contact = ?", "" + user.getId());
            phonesView = (ListView) findViewById(R.id.phones);
            if (phoneNumbers != null) {
                phoneNumberAdapter = new PhoneNumberAdapter(this, R.layout.phone_numbers_list, phoneNumbers, true);
                if (phonesView != null) {
                    phonesView.setAdapter(phoneNumberAdapter);
                }
                phonesView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        Mobile mobile = (Mobile) phonesView.getItemAtPosition(position);
                        if (mobile != null) {
                            mobile.delete();
                            refreshPhonesList();
                        }
                        return false;
                    }
                });
                refreshPhonesList();

            }
            mails = (ArrayList<Mail>) Mail.find(Mail.class, "contact = ?", "" + user.getId());
            mailsView = (ListView) findViewById(R.id.mails);
            if (mails != null) {
                mailAdapter = new MailAdapter(this, R.layout.emails_list, mails, true);
                if (mailsView != null) {
                    mailsView.setAdapter(mailAdapter);
                }
                mailsView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        Mail mail = (Mail) mailsView.getItemAtPosition(position);
                        if (mail != null) {
                            mail.delete();
                            refreshMailList();
                        }
                        return false;
                    }
                });
                refreshMailList();
            }
        }

        ImageView deleteUser = (ImageView) findViewById(R.id.delete_user);

        if (deleteUser != null) {
            deleteUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean status = Contact.delete(user);
                    if (status) {
                        Intent returnIntent = new Intent();
                        setResult(RESULT_CANCELED, returnIntent);
                        finish();
                    }
                }
            });
        }

        ImageView approve = (ImageView) findViewById(R.id.approve_user);

        if (approve != null) {
            approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = contactName.getText().toString();
                    user.setName(name);
                    user.save();
                    Intent returnIntent = new Intent();
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            });
        }

        ImageView newMail = (ImageView) findViewById(R.id.add_mail);

        if (newMail != null) {
            newMail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditingActivity.this, AddMailActivity.class);
                    intent.putExtra("userID", user.getId());
                    startActivityForResult(intent, 1);
                }
            });
        }

        ImageView newNumber = (ImageView) findViewById(R.id.add_number);

        if (newNumber != null) {
            newNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EditingActivity.this, AddNumberActivity.class);
                    intent.putExtra("userID", user.getId());
                    startActivityForResult(intent, 1);
                }
            });
        }

    }

    private void refreshPhonesList() {
        phoneNumbers = (ArrayList<Mobile>) Mobile.find(Mobile.class, "contact = ?", "" + user.getId());
        if (phoneNumbers != null) {
            if (phoneNumberAdapter == null) {
                phoneNumberAdapter = new PhoneNumberAdapter(this, R.layout.phone_numbers_list, phoneNumbers, true);
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
                mailAdapter = new MailAdapter(this, R.layout.emails_list, mails, true);
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
//            finish();
        }

        if (resultCode == RESULT_OK) {
            refreshMailList();
            refreshPhonesList();
        }
    }

}
