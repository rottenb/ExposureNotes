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

    FrameArrayAdapter(Context context, List<FrameData> frameData) {
        super(context, 0, frameData);
    }

    @Override
    public @NonNull View getView(int position, View view, @NonNull ViewGroup parent) {
        FrameData frame = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.frame_list_item,
                    parent,false);
        }

        // Populate the list view with information
        if (frame != null) {
            TextView itemView = view.findViewById(R.id.number);
            itemView.setText(String.format(Locale.getDefault(), "%d", position + 1));

            itemView = view.findViewById(R.id.shutter);
            itemView.setText(frame.getShutter());

            itemView = view.findViewById(R.id.aperture);
            itemView.setText(frame.getAperture());

            itemView = view.findViewById(R.id.notes);
            itemView.setText(frame.getNotes());
        }

        return view;
    }

}
