package com.brianmk.exposurenotes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FilmSettingsDialog extends DialogFragment {
    private static final String LOG_TAG = FilmSettingsDialog.class.getSimpleName();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final View content = getActivity().getLayoutInflater().inflate(R.layout.film_settings_dialog,
                                                                        null);
        builder.setView(content);

        ((EditText) content.findViewById(R.id.camera_type)).setText(getArguments().getString("camera"));
        ((EditText) content.findViewById(R.id.lens_type)).setText(getArguments().getString("lens"));
        ((EditText) content.findViewById(R.id.film_type)).setText(getArguments().getString("film"));
        ((EditText) content.findViewById(R.id.iso_value)).setText(getArguments().getString("iso"));
        ((EditText) content.findViewById(R.id.dev_value)).setText(getArguments().getString("dev"));

        Button okButton = content.findViewById(R.id.film_ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String c = ((EditText) content.findViewById(R.id.camera_type)).getText().toString();
                String l = ((EditText) content.findViewById(R.id.lens_type)).getText().toString();
                String f = ((EditText) content.findViewById(R.id.film_type)).getText().toString();
                String i = ((EditText) content.findViewById(R.id.iso_value)).getText().toString();
                String d = ((EditText) content.findViewById(R.id.dev_value)).getText().toString();
                ((MainActivity)getActivity()).setFilmData(c, l, f, i, d);
                dismiss();
            }
        });

        return builder.create();
    }
}
