package com.contacts.adapters;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.contacts.R;
import com.contacts.models.Contact;

import java.util.ArrayList;

public class UserListAdapter extends ArrayAdapter<Contact> {

    private final Activity context;
    private final ArrayList<Contact> users;

    public UserListAdapter(Activity context, ArrayList<Contact> users) {
        super(context, R.layout.list_item_view, users);
        this.users = users;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        // reuse views
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.list_item_view, null);
            // configure view holder
            UserViewHolder viewHolder = new UserViewHolder();

            if (rowView != null) {
                viewHolder.nameView = (TextView) rowView.findViewById(R.id.name_view);
                viewHolder.imageView = (ImageView) rowView.findViewById(R.id.image_view);

                rowView.setTag(viewHolder);
            }
        }

        // fill data
        if (rowView != null) {
            final UserViewHolder holder = (UserViewHolder) rowView.getTag();
            final Contact user = users.get(position);
            if (user != null) {

                holder.nameView.setText(user.getName());
                //TODO
                holder.imageView.setImageResource(R.mipmap.ic_launcher);
            }
        }

        return rowView;
    }

    @Override
    public long getItemId(int position) {
        Contact item = getItem(position);
        return item.getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    static class UserViewHolder {
        ImageView imageView;
        TextView nameView;
    }

}
