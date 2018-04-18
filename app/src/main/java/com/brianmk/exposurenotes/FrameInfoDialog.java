package com.brianmk.exposurenotes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class FrameInfoDialog extends DialogFragment {
    private static final String LOG_TAG = FrameInfoDialog.class.getSimpleName();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View content = inflater.inflate(R.layout.frame_dialog, null);

        builder.setView(content);

        // Shutter spinner
        final Spinner shutSpin = content.findViewById(R.id.set_shutter);
        ArrayAdapter<CharSequence> shutAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.shutter_speeds, R.layout.shutter_spinner_item);
        shutAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shutSpin.setAdapter(shutAdapter);
        shutSpin.setSelection(getArguments().getInt("shutter"));

        // Aperture spinner
        final Spinner aptSpin = content.findViewById(R.id.set_aperture);
        ArrayAdapter<CharSequence> aptAdapter = ArrayAdapter.createFromResource( getContext(),
                R.array.apertures, R.layout.shutter_spinner_item);
        aptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aptSpin.setAdapter(aptAdapter);
        aptSpin.setSelection(getArguments().getInt("aperture"));

        // Notes
        final EditText notesEdit = content.findViewById(R.id.detail_notes);
        notesEdit.setText(getArguments().getString("notes"));

        // Save info and return to main screen
        Button okButton = content.findViewById(R.id.frame_ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int pos = getArguments().getInt("position");
                int t = shutSpin.getSelectedItemPosition();
                int a = aptSpin.getSelectedItemPosition();
                String n = notesEdit.getText().toString();

                ((MainActivity)getActivity()).setSingleFrameData(pos, t, a, n);

                dismiss();
            }
        });

        return builder.create();
    }
}
