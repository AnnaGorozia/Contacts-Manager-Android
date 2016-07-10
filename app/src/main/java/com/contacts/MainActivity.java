package com.contacts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.contacts.adapters.UserListAdapter;
import com.contacts.models.Contact;
import com.contacts.models.Mail;
import com.contacts.models.Mobile;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private ListView list;
    private UserListAdapter adapter;
    private ArrayList<Contact> userList;
    private EditText etSearch;
    private List<Contact> allContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Contact contact = new Contact("Anano Bodokia");
//        contact.save();
//        Mail mail = new Mail(contact, "anano.bodokia@mail.com");
//        mail.save();
//        Mail mail1 = new Mail(contact, "abodo12@freeuni.edu.ge");
//        mail1.save();
//        Mobile mobile = new Mobile(contact, "599403211");
//        mobile.save();




        userList = new ArrayList<Contact>();
        list = (ListView) findViewById(R.id.listView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Contact user = (Contact) list.getItemAtPosition(position);
                if (user != null) {
                    Intent intent = new Intent(MainActivity.this, ViewingActivity.class);
                    intent.putExtra("userID", user.getId());
                    startActivityForResult(intent, 1);
                }
            }
        });

        refreshUserList();

        etSearch = (EditText) findViewById(R.id.search_text);
        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null) {
                    list.setAdapter(null);
                    adapter.clear();
                    adapter.addAll(allContacts);
                    adapter.notifyDataSetChanged();
                    list.setAdapter(adapter);
                } else {
                    ArrayList<Contact> filtered = new ArrayList<Contact>();
                    for (Contact c : allContacts) {
                        if (c.getName().toLowerCase().startsWith(s.toString().toLowerCase()) || isContactNumber(c.getMobiles(), s)) {
                            filtered.add(c);
                        }
                    }
                    list.setAdapter(null);
                    adapter.clear();
                    adapter.addAll(filtered);
                    adapter.notifyDataSetChanged();
                    list.setAdapter(adapter);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        ImageView addNew = (ImageView) findViewById(R.id.add_new);
        if (addNew != null) {
            addNew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, AddNewContactActivity.class);
                    startActivityForResult(intent, 1);
                }
            });
        }
    }

    private boolean isContactNumber(List<Mobile> mobiles, CharSequence s) {
        for (Mobile mobile : mobiles) {
            if (mobile.getName().startsWith(s.toString()))
                return true;
        }
        return false;
    }

    private void refreshUserList() {
        userList = (ArrayList<Contact>) Contact.listAll(Contact.class);
        if (userList != null) {
            if (adapter == null) {
                adapter = new UserListAdapter(MainActivity.this, userList);
                list.setAdapter(adapter);
            } else {
                list.setAdapter(null);
                adapter.clear();
                adapter.addAll(userList);
                adapter.notifyDataSetChanged();
                list.setAdapter(adapter);
            }
        }
        allContacts = new ArrayList<>();
        allContacts.addAll(userList);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        refreshUserList();
    }
}
