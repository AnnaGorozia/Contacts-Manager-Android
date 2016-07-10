package com.contacts.adapters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.contacts.R;
import com.contacts.models.Mobile;

import java.util.ArrayList;

public class PhoneNumberAdapter extends ArrayAdapter<Mobile> {

    private ArrayList<Mobile> list;
    private Context context;
    private SparseBooleanArray mSelectedItemsIds;
    private boolean editable;

    public PhoneNumberAdapter(Context context, int id, ArrayList<Mobile> list, boolean editable) {
        super(context, id, list);
        mSelectedItemsIds = new SparseBooleanArray();
        this.context = context;
        this.list = list;
        this.editable = editable;
    }

    @Override
    public void remove(Mobile string) {
        list.remove(string);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View rootView;
        ViewHolder viewHolder = null;
        if (convertView == null) {
            rootView = View.inflate(context, R.layout.phone_numbers_list, null);
            TextView numberView = (TextView) rootView.findViewById(R.id.phone_number);
//            numberView.setFocusable(editable);
            viewHolder = new ViewHolder();
            viewHolder.number = numberView;
            rootView.setTag(viewHolder);
        } else {
            rootView = convertView;
            viewHolder = (ViewHolder) rootView.getTag();
        }


        final Mobile fileElement = (Mobile) getItem(position);
        final String phone = fileElement.getName();
        viewHolder.number.setText(fileElement.getName());


        ImageView sms = (ImageView) rootView.findViewById(R.id.sms);

        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", phone, null)));
            }
        });

        if (editable) {
            sms.setVisibility(View.INVISIBLE);
        }

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
        TextView number;
    }
}
