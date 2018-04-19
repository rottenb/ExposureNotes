package com.brianmk.exposurenotes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
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
    public @NonNull View getView(int pos, View view, @NonNull ViewGroup parent) {
        FrameData frame = getItem(pos);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.frame_list_item,
                    parent,false);
        }

        // Populate the list view with information
        if (frame != null) {
            TextView frameNumberView = view.findViewById(R.id.number);
            if ((frame.getShutterIdx() > 0) && (frame.getApertureIdx() > 0)) {
                frameNumberView.setTextColor(ContextCompat.getColor(getContext(), R.color.dark_grey));
                ((TextView) view.findViewById(R.id.number)).setText(String.format(Locale.getDefault(), "%d", pos + 1));
            } else {
                frameNumberView.setTextColor(ContextCompat.getColor(getContext(), R.color.lighter_grey));

                ((TextView) view.findViewById(R.id.number)).setText(String.format(Locale.getDefault(), "%d", pos + 1));
            }

            ((TextView) view.findViewById(R.id.shutter)).setText(frame.getShutter());
            ((TextView) view.findViewById(R.id.aperture)).setText(frame.getAperture());
            ((TextView) view.findViewById(R.id.notes)).setText(frame.getNotes());


        } else {
            Log.d(LOG_TAG, "frame #" + pos + " is null.");
        }

        return view;
    }

}
