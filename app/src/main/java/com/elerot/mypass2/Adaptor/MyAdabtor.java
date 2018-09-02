package com.elerot.mypass2.Adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.elerot.mypass2.Class.data;
import com.elerot.mypass2.R;

import java.util.ArrayList;

/**
 * Created by selcuk.celik on 23.12.2015.
 */
public class MyAdabtor extends ArrayAdapter<data> {
    public MyAdabtor(Context context, ArrayList<data> datas) {
        super(context, 0, datas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final data data = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.data_item, parent, false);
        }
        // Lookup view for data population
        TextView tvid = (TextView) convertView.findViewById(R.id.tvwID);
        final CheckBox cb = (CheckBox) convertView.findViewById(R.id.tvwCheckBox);
        TextView dn = (TextView) convertView.findViewById(R.id.tvwDisplayName);
        TextView un = (TextView) convertView.findViewById(R.id.tvwUserName);
        TextView p = (TextView) convertView.findViewById(R.id.tvwPass);
        TextView d = (TextView) convertView.findViewById(R.id.tvwDescription);
        // Populate the data into the template view using the data object
        tvid.setText(String.valueOf(data._id));
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    data.cbChecked = cb.isChecked();
            }
        });
        cb.setChecked(data.cbChecked);
        cb.setVisibility(data.vsblty);
        dn.setText(data.displayName);
        un.setText(data.userName);
        p.setText(data.pass);
        d.setText(data.description);
        // Return the completed view to render on screen
        return convertView;
    }
}

