package com.brianmk.exposurenotes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class FilmDataAdapter extends ArrayAdapter<FilmData> {
    private static final String LOG_TAG = FilmDataAdapter.class.getSimpleName();

    FilmDataAdapter(Context c, List<FilmData> filmData) {
        super(c, 0, filmData);
    }

    @Override
    public @NonNull View getView(int pos, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.activity_main,
                    parent,false);
        }

        TextView camera = view.findViewById(R.id.camera_type);


        return view;
    }


}
