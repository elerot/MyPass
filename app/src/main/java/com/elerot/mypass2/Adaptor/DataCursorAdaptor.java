package com.elerot.mypass2;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by selcuk.celik on 22.12.2015.
 */
public class DataCursorAdaptor extends CursorAdapter {
    public DataCursorAdaptor(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View retView = inflater.inflate(R.layout.data_item, parent, false);
        return retView;
        //return LayoutInflater.from(context).inflate(R.layout.data_item, parent, false);
    }
    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        int id = cursor.getInt(0);
        String displayName = cursor.getString(1);
        String username = cursor.getString(2);
        String pass = cursor.getString(3);
        String description = cursor.getString(4);

        TextView tvID = (TextView)view.findViewById(R.id.tvwID);
        CheckBox tvCheckBox = (CheckBox)view.findViewById(R.id.tvwCheckBox);
        TextView tvDisplayName = (TextView) view.findViewById(R.id.tvwDisplayName);
        TextView tvUserName = (TextView) view.findViewById(R.id.tvwUserName);
        TextView tvPass = (TextView)view.findViewById(R.id.tvwPass);
        TextView tvDescription = (TextView)view.findViewById(R.id.tvwDescription);

        tvID.setText(String.valueOf(id));
        tvCheckBox.setChecked(false);
        tvCheckBox.setVisibility(View.INVISIBLE);
        tvDisplayName.setText(displayName);
        tvUserName.setText(username);
        tvPass.setText(pass);
        tvDescription.setText(description);
    }
}
