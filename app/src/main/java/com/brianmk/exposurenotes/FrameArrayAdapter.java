package com.brianmk.exposurenotes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class FrameArrayAdapter extends ArrayAdapter<FrameData> {
    private static final String LOG_TAG = FrameArrayAdapter.class.getSimpleName();

    public FrameArrayAdapter(Context context, List<FrameData> frameData) {
        super(context, 0, frameData);
    }


    @Override
    public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent) {
        FrameData frame = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.frame_list_item,
                    parent,false);
        }

        TextView itemView = convertView.findViewById(R.id.number);
        itemView.setText(String.format(Locale.getDefault(),"%d", position + 1));
        itemView = convertView.findViewById(R.id.shutter);
        itemView.setText(frame.getShutter());
        itemView = convertView.findViewById(R.id.aperture);
        itemView.setText(frame.getAperture());
        itemView = convertView.findViewById(R.id.notes);
        itemView.setText(frame.getNotes());
/*
        if (position % 2 == 1) {
            convertView.setBackgroundColor(Color.BLUE);
        } else {
            convertView.setBackgroundColor(Color.CYAN);
        }
*/

        return convertView;
    }

}
