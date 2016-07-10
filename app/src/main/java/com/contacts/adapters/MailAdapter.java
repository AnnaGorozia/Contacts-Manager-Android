package com.contacts.adapters;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.contacts.R;
import com.contacts.models.Mail;

import java.util.ArrayList;

public class MailAdapter extends ArrayAdapter<Mail> {

    private ArrayList<Mail> list;
    private Context context;
    private SparseBooleanArray mSelectedItemsIds;
    private boolean editable;

    public MailAdapter(Context context, int id, ArrayList<Mail> list, boolean editable){
        super(context, id, list);
        mSelectedItemsIds = new SparseBooleanArray();
        this.context = context;
        this.list = list;
        this.editable = editable;
    }

    @Override
    public void remove(Mail string) {
        list.remove(string);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView;
        ViewHolder viewHolder = null;
        if(convertView == null){
            rootView = View.inflate(context, R.layout.emails_list, null);
            TextView mailView = (TextView) rootView.findViewById(R.id.email);
//            mailView.setFocusable(editable);
            viewHolder = new ViewHolder();
            viewHolder.mail = mailView;
            rootView.setTag(viewHolder);
        }else{
            rootView = convertView;
            viewHolder = (ViewHolder) rootView.getTag();
        }

        Mail fileElement = (Mail) getItem(position);

        viewHolder.mail.setText(fileElement.getName());

        return rootView;
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    private class ViewHolder {
        TextView mail;
    }
}
