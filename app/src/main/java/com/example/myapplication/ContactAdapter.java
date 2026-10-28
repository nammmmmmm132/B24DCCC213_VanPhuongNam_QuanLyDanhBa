package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/** Adapter hiển thị mỗi liên hệ trên một dòng của ListView. */
public class ContactAdapter extends ArrayAdapter<Contact> {

    public ContactAdapter(@NonNull Context context, @NonNull ArrayList<Contact> contacts) {
        super(context, 0, contacts);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_contact, parent, false);
        }
        Contact c = getItem(position);
        if (c != null) {
            ((TextView) view.findViewById(R.id.tvAvatar)).setText(c.getInitial());
            ((TextView) view.findViewById(R.id.tvName)).setText(c.getFullName());
            ((TextView) view.findViewById(R.id.tvPhone)).setText(c.getPhone());
            ((TextView) view.findViewById(R.id.tvGroup)).setText(c.getGroup());
            view.findViewById(R.id.tvFavorite)
                    .setVisibility(c.isFavorite() ? View.VISIBLE : View.INVISIBLE);
        }
        return view;
    }
}
